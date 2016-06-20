package org.nuxeo.gatling.marklogic

import io.gatling.core.session._

/**
  * @param requestName the name of the request
  */
class Xcc(requestName: String) {

  def uri(uri: Expression[String]): XccUri = XccUri(requestName, uri)

  def search(request: Expression[String]): XccMarkLogicSearchBuilder = XccMarkLogicSearchBuilder(requestName, request)

}

case class XccUri(requestName: String, uri: Expression[String]) {

  def insert(content: Expression[String]): XccMarkLogicInsertBuilder = XccMarkLogicInsertBuilder(requestName, uri, content)

  def get(): XccMarkLogicGetBuilder = XccMarkLogicGetBuilder(requestName, uri)

}
