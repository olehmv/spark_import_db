package com.epam.db.core.imprt.utils
import com.epam.db.core.imprt.param.JDBCParam

import scala.xml.{Elem, XML}

object Utils {

  def parseJDBCConf(file: String): JDBCParam = {
    val node: Elem = XML.loadFile(file)
    JDBCParam.fromXML(node)
  }

}
