import com.epam.db.core.imprt.param.{JDBCParam, TableType, URL, User}
import org.apache.spark.sql.ColumnName

import scala.xml.XML

object Test {

  def main(args: Array[String]): Unit = {

    val user = new User("sa", "asus")

    val url = new URL("localhost", "1433", "AdventureWorks2012")

    val tableType = new TableType("BASE TABLE")

    val jdbcParam = new JDBCParam(_url = url,
                                  _user = user,
                                  _driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver",
                                  _dbInfoSchemaTable = "INFORMATION_SCHEMA.TABLES",
                                  _tableType = tableType :: Nil)

    val elem = XML
      .loadString(
        new scala.xml.PrettyPrinter(80, 2)
          .formatNodes(jdbcParam.toXml)
      )
    XML.save("src/test/resources/param.xml", elem, "UTF-8", xmlDecl = true)

  }

}
