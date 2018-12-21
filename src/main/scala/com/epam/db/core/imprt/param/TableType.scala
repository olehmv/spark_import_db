package com.epam.db.core.imprt.param

import scala.xml.NodeSeq

class TableType(_value: String){

  // Getter
  def value = _value

  // Setter
  def value_ = _value

  def toXML =
    <tabletype>
    {_value}
    </tabletype>

  override def toParam: TableType=
    <tabletype>
    {_value}
    </tabletype>

  override def fromParam(param: AnyRef): TableType =
    new TableType(
      _value = param.asInstanceOf[NodeSeq].text
    )
}

object TableType {

  def fromXML(node: NodeSeq): TableType =
    new TableType(
      _value = node.text
    )
}
