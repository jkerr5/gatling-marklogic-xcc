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

import akka.actor.ActorDSL._
import akka.actor.ActorRef
import io.gatling.core.config.Protocols
import io.gatling.core.session._

case class XccFindOneSearchBuilder(
	requestName: String,
	key1: String,
	value1: String,
	key2: String,
	value2: String,
	ignored: Array[String]
) extends MarkLogicActionBuilder {

	override def build(next: ActorRef, protocols: Protocols): ActorRef = {

		actor(actorName(requestName)) {
			new XccFindOneSearchCall(requestName, key1, value1, key2, value2, ignored, xccProtocol(protocols), next)
		}
	}

}
