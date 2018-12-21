import AssemblyKeys._

name := "spark_import_db"

version := "0.1"

scalaVersion := "2.11.11"

val sparkVersion = "2.3.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion ,
  "org.apache.spark" %% "spark-hive" % sparkVersion ,
  "com.microsoft.sqlserver" % "mssql-jdbc" % "6.4.0.jre8",
  "org.scalatest"    %% "scalatest"           % "3.0.5" % "test"
)
// This statement includes the assembly plug-in capabilities
assemblySettings
// Configure JAR used with the assembly plug-in
jarName in assembly := "import_db.jar"
// A special option to exclude Scala itself form our assembly JAR, since Spark
// already bundles Scala.
assemblyOption in assembly :=
  (assemblyOption in assembly).value.copy(includeScala = false)