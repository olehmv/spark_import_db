package com.epam.db.core.imprt.param

import scala.xml.NodeSeq

class JDBCParam(_driver: String,
                _url: URL,
                _user: User,
                _dbInfoSchemaTable: String,
                _tableType: List[TableType]) extends Serializable {

  // Getter
  def driver    = _driver
  def url       = _url
  def user      = _user
  def dbInfoSchemaTable = _dbInfoSchemaTable
  def tableType = _tableType

  //Setter
  def driver_    = _driver
  def url_       = _url
  def user_      = _user
  def dbInfoSchemaTable_ = _dbInfoSchemaTable
  def tableType_ = _tableType

  def toXml =
    <jdbcconf driver={_driver} dbInfoSchemaTable={_dbInfoSchemaTable}>
      {_url.toXML}
      {_user.toXML}
      {for(elem<-_tableType)yield elem.toXML}
    </jdbcconf>

}
object JDBCParam {

  def fromXML(node: NodeSeq) =
    new JDBCParam(
      _driver = (node \ "@driver") text,
      _url = URL.fromXML(node \ "url"),
      _user = User.fromXML(node \ "user"),
      _dbInfoSchemaTable=(node \ "@dbInfoSchemaTable") text,
      _tableType = for (elem <- (node \ "tabletype").toList) yield TableType.fromXML(elem)
    )

}
