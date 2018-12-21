package com.epam.db.core.imprt.param

import scala.xml.{ NodeSeq }

class User(_user: String, _password: String) {

  // Getter
  def user     = _user
  def password = _password
  // Setter
  def password_ = _password
  def user_     = _user

  def toXML =
    <user user={_user} password={_password}/>

}

object User {

  def fromXML(node: NodeSeq): User =
    new User(
      _user = (node \ "@user") text,
      _password = (node \ "@password") text
    )

}
