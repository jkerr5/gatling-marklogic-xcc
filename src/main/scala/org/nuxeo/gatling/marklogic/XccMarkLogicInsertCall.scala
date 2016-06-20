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
import com.marklogic.xcc.ContentFactory
import io.gatling.core.action.Chainable
import io.gatling.core.result.message._
import io.gatling.core.result.writer.DataWriterClient
import io.gatling.core.session._
import io.gatling.core.util.TimeHelper

class XccMarkLogicInsertCall(requestName: String, uri: Expression[String], content: Expression[String], protocol: XccMarkLogicProtocol, val next: ActorRef)
  extends Chainable with DataWriterClient {

  override def execute(session: Session): Unit = {
    val start = TimeHelper.nowMillis
    val result = protocol.call(ContentFactory.newContent(uri(session).get, content(session).get, null))
    val end = TimeHelper.nowMillis
    if (result == "")
      writeRequestData(session, requestName, start, start, end, end, OK)
    else
      writeRequestData(session, requestName, start, start, end, end, KO, Some(result))
    next ! session
  }

}
