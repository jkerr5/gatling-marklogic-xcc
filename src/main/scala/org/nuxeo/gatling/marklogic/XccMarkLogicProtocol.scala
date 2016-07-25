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

import java.net.URI

import com.marklogic.xcc.exceptions.RequestException
import com.marklogic.xcc.{AdhocQuery, Content, ContentSourceFactory, Request}
import io.gatling.core.config.Protocol

object XccMarkLogicProtocol {

  val DefaultXccProtocol = XccMarkLogicProtocol("xcc://root:root@localhost:8000")

}

case class XccMarkLogicProtocol(uri: String) extends Protocol {

  //val session = ContentSourceFactory.newContentSource(new URI(uri)).newSession()
  //val source = ContentSourceFactory.newContentSource(new URI(uri))

  // these have to be fixed to use the session correctly
  def call(content: Content): String = {
    try {
      //session.insertContent(content)
      ""
    } catch {
      case e: RequestException => e.getMessage
    }
  }

  def call(request: Request): String = {
    try {
      //session.submitRequest(request)
      ""
    } catch {
      case e: RequestException => e.getMessage
    }
  }

  def callAdhocQuery(query: String): String = {
  	val session = source.newSession()
  	try {
    	val request = session.newAdhocQuery(query)
    	val result = session.submitRequest(request)
    	""
    } catch {
    	case e: RequestException => e.getMessage
    } finally {
    	session.close()
    }
  }
}
