package com.epam.db.core.imprt.param

import org.apache.spark.sql.ColumnName

import scala.xml.NodeSeq

class TableType(_name: String)extends Serializable {

  // Getter
  def value = _name

  // Setter
  def value_ = _name

  def toXML =
    <tabletype>
    {_name}
    </tabletype>

}

object TableType {

  def fromXML(node: NodeSeq): TableType =
    new TableType(
      _name = node.text
    )
}
