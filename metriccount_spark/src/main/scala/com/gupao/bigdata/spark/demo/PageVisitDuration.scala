package com.gupao.bigdata.spark.demo

import java.text.SimpleDateFormat

import org.apache.spark.{SparkConf, SparkContext}
import org.eclipse.jetty.util.{MultiMap, UrlEncoded}

object PageVisitDuration {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName("Page Visited Duration")
      .setMaster("local[2]")
    val sc = new SparkContext(conf)
    // 读取日志
    val logDataRDD = sc.textFile("nginx_example.txt")
    // 初步处理日志
    val splitLogDataRDD = logDataRDD
      .map(_.split("\t"))
      .filter(logFields => logFields(1).contains("product_id") && logFields(3).contains("uid"))
      .map(logFields => {
        val paramsMap = new MultiMap[String]
        UrlEncoded.decodeTo(logFields(1), paramsMap, "UTF-8")
        val productId = paramsMap.getValue("product_id", 0);
        val uid = logFields(3).split("=")(1)
        val timestamp = logFields(4)
        (uid, (productId, timestamp))
      })
    // 解析时间
    val parsedTimeLogDataRDD = splitLogDataRDD.mapPartitions(eachPartition => { // 解析时间
      val sdf = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z")
      eachPartition.map(logTuple => {
        val timestampLong = sdf.parse(logTuple._2._2).getTime
        (logTuple._1, (logTuple._2._1, timestampLong))
      })
    })
    // 根据uid进行分组
    val pageVisitedDurationRDD = parsedTimeLogDataRDD
      .groupByKey()
      .flatMap(groupedVisitedLogWithUID => {
        // 用户id
        val uid = groupedVisitedLogWithUID._1
        // 每个页面的时间集合
        val pageVisitedCollections = groupedVisitedLogWithUID._2
        // 对时间集合进行排序
        val sortedPageVisitedCollections = pageVisitedCollections.toList.sortBy(_._2)
        // 计算时间差
        for (i <- 0 until (sortedPageVisitedCollections.size - 1)) yield {
          val pageId = sortedPageVisitedCollections(i)._1
          val beginTime = sortedPageVisitedCollections(i)._2
          val endTime = sortedPageVisitedCollections(i + 1)._2
          val diff = (endTime - beginTime) / 1000
          (uid, (pageId, diff))
        }
      })
    // Only Debug
    pageVisitedDurationRDD.collect().foreach(println)
    // 对结果计算平均值
    val avgResultRDD = pageVisitedDurationRDD
      .map(pageVisitedTuple => (pageVisitedTuple._2._1, pageVisitedTuple._2._2))
      .aggregateByKey((0L, 0L))((acc, value) => (acc._1 + value, acc._2 + 1L),
        (acc1, acc2) => (acc1._1 + acc2._1, acc1._2 + acc2._2))
      .mapValues(x => (x._1.toDouble / x._2.toDouble))
    // Only Debug
    avgResultRDD.collect().foreach(println)
  }

}
