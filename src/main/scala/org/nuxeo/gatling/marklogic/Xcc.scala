package org.nuxeo.gatling.marklogic

import io.gatling.core.session._

/**
  * @param requestName the name of the request
  */
class Xcc(requestName: String) {

  def uri(uri: Expression[String]): XccUri = XccUri(requestName, uri)

  def search(request: Expression[String]): XccMarkLogicSearchBuilder = XccMarkLogicSearchBuilder(requestName, request)

  def findOne(
		key1: String,
		value1: String,
		key2: String,
		value2: String,
		ignored: Array[String]
  ): XccFindOneSearchBuilder = XccFindOneSearchBuilder(requestName, key1, value1, key2, value2, ignored)

  def update(
		uri: String,
		patchSpec: String
  ): XccUpdateBuilder = XccUpdateBuilder(requestName, uri, patchSpec)

}

case class XccUri(requestName: String, uri: Expression[String]) {

  def insert(content: Expression[String]): XccMarkLogicInsertBuilder = XccMarkLogicInsertBuilder(requestName, uri, content)

  def get(): XccMarkLogicGetBuilder = XccMarkLogicGetBuilder(requestName, uri)

}
