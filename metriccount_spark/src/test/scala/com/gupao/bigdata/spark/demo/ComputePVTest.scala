package com.gupao.bigdata.spark.demo

import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfter, FunSuite}

class ComputePVTest extends FunSuite with BeforeAndAfter {

  private val master = "local[2]"
  private val appName = "ComputePV-Test"

  private var sc: SparkContext = _

  before {
    val conf = new SparkConf()
      .setMaster(master)
      .setAppName(appName)

    sc = new SparkContext(conf)
  }

  after {
    if (sc != null) {
      sc.stop()
    }
  }

  test("pv compute") {
    // ip,url,ref_url,cookie,time_stamp
    val testLogRDD = sc.textFile("nginx_example.txt")
    val resultRDD = ComputePV.computePV(testLogRDD)
    val result = resultRDD.collect()
    val keyValueMap = result.toMap
    println(keyValueMap)
  }

}
