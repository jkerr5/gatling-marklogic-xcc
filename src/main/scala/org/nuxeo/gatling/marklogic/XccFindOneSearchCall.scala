/*
 * (C) Copyright 2016 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Kevin Leturc
 */
package org.nuxeo.gatling.marklogic

import akka.actor.ActorRef
import io.gatling.core.action.Chainable
import io.gatling.core.result.message._
import io.gatling.core.result.writer.DataWriterClient
import io.gatling.core.session._
import io.gatling.core.util.TimeHelper

import com.marklogic.xcc.exceptions.RequestException
import com.marklogic.xcc.{AdhocQuery, Content, ContentSourceFactory, Request}


class XccFindOneSearchCall(
	requestName: String,
	key1: String,
	value1: String,
	key2: String,
	value2: String,
	ignored: Array[String],
	protocol: XccMarkLogicProtocol,
	val next: ActorRef
)
extends Chainable with DataWriterClient {

	val query =
"""
declare variable $key1 as xs:string external;
declare variable $value1 as xs:string external;
declare variable $key2 as xs:string external;
declare variable $value2 as xs:string external;
declare variable $ignored-list as xs:string external;

(: ignored-list is a | delimited list of ids :)
let $not-ids := fn:tokenize($ignored-list, "\|")

let $query :=
		cts:and-query((
			cts:element-range-query(fn:QName("", $key1), "=", $value1),
			cts:element-value-query(fn:QName("", $key2), $value2),
			cts:not-query(
				cts:element-range-query(fn:QName("","ecm__id"), "=", $not-ids)
			)
		))

return (
	cts:search(
		fn:collection(),
		$query
	),
	xdmp:log("search: " ||  fn:seconds-from-duration(xdmp:eval("fn:current-dateTime()") - fn:current-dateTime()))
)
"""

	override def execute(session: Session): Unit = {
		val start = TimeHelper.nowMillis
		//System.out.println("calling search...")
		//val result = protocol.callAdhocQuery(query(session).get)

		val mlSession = protocol.source.newSession()

		try {
			val request = mlSession.newAdhocQuery(query)
			request.setNewStringVariable("key1", key1)
			request.setNewStringVariable("value1", value1)
			request.setNewStringVariable("key2", key2)
			request.setNewStringVariable("value2", value2)
			request.setNewStringVariable("ignored-list", ignored.mkString("|"))

			val resultSeq = mlSession.submitRequest(request)
			val result = ""

			//System.out.println("called search")
			//val result = protocol.call(request)
			val end = TimeHelper.nowMillis
			if (result == "")
				writeRequestData(session, requestName, start, start, end, end, OK)
			else
				writeRequestData(session, requestName, start, start, end, end, KO, Some(result))
			next ! session
		} catch {
			case e: RequestException => e.getMessage
		} finally {
			mlSession.close()
		}

	}

}
