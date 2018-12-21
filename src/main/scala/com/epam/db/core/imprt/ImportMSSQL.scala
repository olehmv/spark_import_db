package com.epam.db.core.imprt

import java.util.Properties

import com.epam.db.core.imprt.param.JDBCParam
import com.epam.db.core.imprt.utils._
import org.apache.spark.sql.SparkSession

object ImportMSSQL {

  def main(args: Array[String]): Unit = {
    if (args.size < 1) {
      println(
        "Usage: \\n" +
        "1 - jdbc param file \\n" +
        "2 - destination directory \\n" +
        "3 - output file format"
      )
      sys.exit(1)
    }
    // Get jdbc parameters
    val param: JDBCParam = Utils.parseJDBCConf(args(0))

    // Get destination directory
    val destDir = args(1)

    // Get output file format (parquet,csv, ect..)
    val outputFormat=args(2)

    // Create spark session
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("")
      .enableHiveSupport()
      .getOrCreate()

    import spark.implicits._

    // Build jdbc url
    val jdbcUrl = s"jdbc:sqlserver://${param.url.host}:${param.url.port}"

    // Create a Properties() object to hold the parameters.
    val connectionProperties = new Properties()
    connectionProperties.put("user", s"${param.user.user}")
    connectionProperties.put("password", s"${param.user.password}")

    // Build database information schema table name
    // The TABLES table provides information about tables in databases.
    val dbSchemaTable = s"${param.url.db}.${param.dbInfoSchemaTable}"

    // Load DB SCHEMA TABLE into DataFrame
    val dbSchemaTableDF = spark.read.jdbc(jdbcUrl, dbSchemaTable, connectionProperties)

    // Column to select and filter on
    val column1 = "TABLE_TYPE"

    // Column to select
    val column2= "TABLE_SCHEMA"

    // Filter table types for extraction
    val tablesForExraction =
      dbSchemaTableDF
        .select($"$column1",$"$column2")
        .filter($"$column1" isin param.tableType)

    // Build table names for extraction
    val tableNamesForExtraction =
      tablesForExraction
        .map(row =>param.url.db+"."+row.getString(0)+"."+row.getString(1))
        .collect()

    // Extract tables to DataFrame
    val tablesDF = tableNamesForExtraction.map(table=>spark.read.jdbc(jdbcUrl,table,connectionProperties))

    // Import tables to destination directory
    tablesDF.foreach(df=>df.write.format(outputFormat).option("path",destDir))

  }

}
