package com.sapient.session


import com.sapient.session.UserSession.{spark, userWindow}
import com.sapient.spark.Constants.{APP_NAME, USER_SESSION_FILE_PATH, USER_FILE_PATH}
import com.sapient.spark.WithSpark
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{col, lag, row_number, unix_timestamp, when,to_date,count,sum,substring}

import org.apache.spark.sql.types.DataTypes

object SessionAnalysis extends App {

   /*spark set up */
  val spark = WithSpark.getSpark("session analysis")
  spark.sparkContext.setLogLevel("Error")
  import spark.implicits._
  val userWindow = Window.partitionBy("userId").orderBy("timestamplong")
  val userSessionWindow = Window.partitionBy("userId", "session").orderBy("timestamplong")

  /* Reading Enriched Input data here */
  val inputUserData = spark.read.option("header", "true").csv(USER_SESSION_FILE_PATH)


  /* get number of session generated in a day */
   inputUserData.groupBy(to_date($"timestamp").as("date")).agg(count($"session").as("total_session_day")).show(false)


  /* Total time spent by a user in a day */


 val userSessionTime = inputUserData.withColumn("timestamplong", unix_timestamp($"timestamp", "yyyy-MM-dd'T'HH:mm:ss'Z'")).
   withColumn("ts_diff", (col("timestamplong") - lag("timestamplong", 1).over(userWindow)) / 60).na.fill(0.0).
   withColumn("date",to_date($"timestamp")).
   withColumn("dateLag", lag(to_date($"timestamp"), 1).over(userWindow)).
   withColumn("ts_diff",when(col("date") === $"dateLag", $"ts_diff").otherwise("0"))


 userSessionTime.groupBy(to_date($"timestamp").as("date"),$"userId").agg(sum($"ts_diff").as("total_time_hour")/60).show(false)

  /* Total time spent by a user over a month  */

   userSessionTime.groupBy(substring(to_date($"timestamp"),0,7).as("date"),$"userId").agg(sum($"ts_diff").as("total_time_hour")/60).show(false)
}
