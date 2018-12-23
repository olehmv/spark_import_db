package com.epam.db.core.imprt.param


import scala.xml.NodeSeq

class URL(_host: String, _port: String, _db: String) extends Serializable {

  // Getter
  def host = _host
  def port = _port
  def db   = _db

  // Setter
  def host_ = _host
  def port_ = _port
  def db_    = _db

  def toXML =
    <url host={_host} port={_port} db={_db} />

}

object URL {

  def fromXML(node: NodeSeq): URL =
    new URL(
      _host = (node \ "@host") text,
      _port = (node \ "@port") text,
      _db = (node \ "@db") text
    )
}
