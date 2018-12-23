package com.epam.db.core.imprt

import java.util.Properties

import com.epam.db.core.imprt.param.JDBCParam
import com.epam.db.core.imprt.utils._
import org.apache.spark.sql.{ SparkSession }
import org.apache.spark.sql.functions._

object ImportMSSQL {

  def main(args: Array[String]): Unit = {
    if (args.size < 1) {
      println(
        "Usage: \n" +
        "1 - jdbc param file \n" +
        "2 - destination directory \n" +
        "3 - output file format"
      )
      sys.exit(1)
    }
    // Get jdbc parameters
    val param: JDBCParam = Utils.parseJDBCConf(args(0))

    // Get destination directory
    val destDir = args(1)

    // Get output file format (parquet,csv, ect..)
    val outputFormat = args(2)

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
    connectionProperties.put("user", s"${param.user.name}")
    connectionProperties.put("password", s"${param.user.password}")

    // Build database information schema table name
    // The TABLES table provides information about tables in databases.
    val dbSchemaTable = s"${param.url.db}.${param.dbInfoSchemaTable}"

    // Load DB SCHEMA TABLE into DataFrame
    val dbSchemaTableDF = spark.read.jdbc(jdbcUrl, dbSchemaTable, connectionProperties)

    // Select table names and filter table types for extraction
    val tableNamesForExtraction =
      dbSchemaTableDF
        .select($"TABLE_CATALOG", $"TABLE_SCHEMA", $"TABLE_NAME", $"TABLE_TYPE")
        .filter($"TABLE_TYPE" === "BASE TABLE")
        .withColumn("FULL_TABLE_NAME",
                    concat($"TABLE_CATALOG", lit("."), $"TABLE_SCHEMA", lit("."), $"TABLE_NAME"))
        .drop("TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME", "TABLE_TYPE")
        .map(row => row.getString(0))
        .collect()
        .sortWith(_ < _)

    // Map tables names to extracted table DataFrame
    val tablesDF =
      tableNamesForExtraction
        .map(
          table =>
            (table,
             Utils
               .truncateSpaceInStringColumns(spark.read.jdbc(jdbcUrl, table, connectionProperties)))
        )

    // Import table's DataFrames to destination directory
    // For each table new directory will be created
    tablesDF.foreach(table => table._2.write.format(outputFormat).save(s"${destDir}/${table._1}"))

  }

}
