package com.mask.bigdata.spark.demo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.eclipse.jetty.util.{MultiMap, UrlEncoded}

/**
  * ip,url,cookie,time_stamp
  * spark-submit --master yarn-client --class com.gupao.bigdata.spark.demo.ComputeUV ./spark-demo-1.0-SNAPSHOT.jar /user/root/bill/nginx/input /user/root/bill/nginx/output
  **/
object ComputeUV {

  def computeUV(textRDD: RDD[String]): RDD[(String, Long)] = {
    val splitTextFileRDD = textRDD.map(_.split("\t"))
    val result = splitTextFileRDD
      .filter(log => log(1).contains("product_id") && log(3).contains("uid"))
      .map(log => {
        val paramsMap = new MultiMap[String]
        UrlEncoded.decodeTo(log(1), paramsMap, "UTF-8")
        val productId = paramsMap.getValue("product_id", 0);
        val uid = log(3).split("=")(1)
        (productId, uid)
      })
      .distinct()
      .map(productIdWithUid => (productIdWithUid._1, 1L))
      .reduceByKey(_ + _)
    result
  }

  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      System.err.println("Usage:<input_file> <output_file>")
      System.exit(1)
    }
    val conf = new SparkConf().setAppName("ComputeUV")
    val sc = new SparkContext(conf)
    val textFileRDD = sc.textFile(args(0))
    val result = computeUV(textFileRDD)
    result.map(r => r._1 + "\t" +r._2).saveAsTextFile(args(1))
  }

}