package com.epam.db.core.imprt.param

import scala.xml.{ NodeSeq }

class User(_name: String, _password: String) extends Serializable {

  // Getter
  def name     = _name
  def password = _password
  // Setter
  def password_ = _password
  def name_     = _name

  def toXML =
    <user name={_name} password={_password}/>

}

object User {

  def fromXML(node: NodeSeq): User =
    new User(
      _name = (node \ "@name") text,
      _password = (node \ "@password") text
    )

}
