package com.sapient.spark

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

object WithSpark {
  def getSpark(appName:String): SparkSession = {
    val sparkConf = new SparkConf().setAppName(appName).setMaster("local[4]")
    val spark: SparkSession = SparkSession
      .builder
      .enableHiveSupport()
      .config(sparkConf)
      .getOrCreate()
    spark
  }

  def saveAsParquet(df:DataFrame,path:String)={
    df.repartition(1).write.parquet(path)
  }

  def saveAsCsv(df:DataFrame,path:String)={
    df.repartition(1).write.csv(path)
  }


}
