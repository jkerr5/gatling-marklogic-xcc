package marklogic

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class NuxeoCreateSimulation extends Simulation {
	val httpConf = http
		.baseURL("http://localhost:18080")
		.digestAuth("Administrator", "Administrator")
		.disableCaching

	val search = http("search")
	.post("/nuxeo/site/api/v1/id/00000000-0000-0000-0000-000000000000")
	.body(StringBody("""
{
	"entity-type": "document",
	"name":"newDoc",
	"type": "File",
	"properties": {
		"dc:title": "The new document",
		"dc:description": "Created via a so cool and simple REST API",
		"common:icon": "/icons/file.gif",
		"common:icon-expanded": null,
		"common:size": null
	}
}
		"""))
	.header("Content-Type", "application/json")

	val commitGroup = during(40, "i") {
		randomSwitch(
			100.0 -> exec(search)
		)
	}

	setUp(scenario("nuxeo:create")
		.exec(commitGroup)
		.inject(atOnceUsers(1)))
		.protocols(httpConf)
}
