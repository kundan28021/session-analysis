package com.sapient.session

import com.sapient.session.UserSession.{inputUserData, spark, userWindow}
import com.sapient.spark.Constants.{APP_NAME, USER_FILE_PATH}
import com.sapient.spark.WithSpark
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{col, lag, unix_timestamp}
import org.scalatest.{FlatSpec, Matchers}



class SessionTest extends FlatSpec with Matchers {
  val userWindow = Window.partitionBy("userId").orderBy("timestamplong")
 it should "create a Spark Context" in {
   val spark = WithSpark.getSpark(APP_NAME)
 }

  it should "create a dataframe from csv file" in  {
    val spark = WithSpark.getSpark(APP_NAME)
    val inputUserData = spark.read.option("header", "true").csv(USER_FILE_PATH)
    inputUserData.show(false)
  }

  it should "convert timestamp to long values and add new columns" in {
    val spark = WithSpark.getSpark(APP_NAME)
    import spark.implicits._
    val inputUserData = spark.read.option("header", "true").csv(USER_FILE_PATH)
    inputUserData.
      withColumn("timestamplong", unix_timestamp($"timestamp", "yyyy-MM-dd'T'HH:mm:ss'Z'")).
      show(false)

  }

  it should "create new columns of time difference of user and convert to minutes" in {
    val spark = WithSpark.getSpark(APP_NAME)
    import spark.implicits._
    val inputUserData = spark.read.option("header", "true").csv(USER_FILE_PATH)
    val userSessions = inputUserData.
      withColumn("timestamplong", unix_timestamp($"timestamp", "yyyy-MM-dd'T'HH:mm:ss'Z'")).na.fill(0.0).
      withColumn("ts_diff", (col("timestamplong") - lag("timestamplong", 1).over(userWindow)) / 60).show(false)
  }

  it should "replace null values with Zero" in {
    val spark = WithSpark.getSpark(APP_NAME)
    import spark.implicits._
    val inputUserData = spark.read.option("header", "true").csv(USER_FILE_PATH)
    val userSessions = inputUserData.
      withColumn("timestamplong", unix_timestamp($"timestamp", "yyyy-MM-dd'T'HH:mm:ss'Z'")).na.fill(0.0).
      withColumn("ts_diff", (col("timestamplong") - lag("timestamplong", 1).over(userWindow)) / 60).na.fill(0).show(false)

  }
}
