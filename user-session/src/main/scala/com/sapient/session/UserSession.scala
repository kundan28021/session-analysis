package com.sapient.session

import com.sapient.spark.Constants.{APP_NAME, USER_FILE_PATH,OUTPUT_PATH,WIN_UTILS_PATH}
import com.sapient.spark.WithSpark
import com.sapient.spark.Constants._
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{col, concat, lag, lit, row_number, sum, unix_timestamp, when}
import org.apache.spark.sql.types.DataTypes

object UserSession  extends  App {

  /*to write file on window location*/
  System.setProperty("hadoop.home.dir", WIN_UTILS_PATH)

  /*set up spark context here */
  val spark = WithSpark.getSpark(APP_NAME)
  import spark.implicits._
  spark.sparkContext.setLogLevel("Error")

  /*Reading click stream Input Data from flat file*/
  val inputUserData = spark.read.option("header", "true").csv(USER_FILE_PATH)

  /*Intermediate temporary columns */
  val cols=Seq("timestamplong","ts_diff","aggr_ts_diff","sessionHour","sessionHourLag","SFC")

  /*Created Window with userId and sorted with timestamp long*/
  val userWindow = Window.partitionBy("userId").orderBy("timestamplong")

  val userSessions = inputUserData.
    withColumn("timestamplong", unix_timestamp($"timestamp", "yyyy-MM-dd'T'HH:mm:ss'Z'")).
    withColumn("ts_diff", (col("timestamplong") - lag("timestamplong", 1).over(userWindow)) / 60).na.fill(0.0).
    withColumn("ts_diff", when(row_number.over(userWindow) === 1 || $"ts_diff" >= TIME_OUT_1, 0L).otherwise($"ts_diff")).
    withColumn("aggr_ts_diff",sum("ts_diff").over(userWindow)).
    withColumn("sessionHour",($"aggr_ts_diff"/TIME_OUT_2)).
    withColumn("sessionHourLag",lag(($"sessionHour"),1).over(userWindow).cast(DataTypes.IntegerType)).na.fill(0.0)


  val finalUserSession= userSessions.
    withColumn("SFC",
      when(col("sessionHour") === 0.0, 1).
        when(col("sessionHour").cast(DataTypes.IntegerType) !== $"sessionHourLag" , 1).
        otherwise(0)
    ).withColumn("sessionName" , concat($"userId",lit("-S"),sum($"SFC").over(userWindow)))
    .drop(cols:_*)


  finalUserSession.show(false)

  WithSpark.saveAsParquet(finalUserSession,OUTPUT_PATH);

}
