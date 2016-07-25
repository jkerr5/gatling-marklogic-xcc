package marklogic

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class HTTPCtsSearchSimulation extends Simulation {
	val httpConf = http
		//.baseURL("http://10.93.157.85:8010")
		.baseURL("http://localhost:18010")
		.digestAuth("admin", "systemml")
		.disableCaching

	val search = http("search")
	.post("/ext/findOne.xqy?parent=a68687ef-558c-4990-a8ad-5eebb5b9b778&name=Task2225")
	.body(StringBody("""
		"""))
	.header("Content-Type", "application/xml")

	val commitGroup = during(40, "i") {
		randomSwitch(
			100.0 -> exec(search)
		)
	}

	setUp(scenario("rest:search")
		.exec(commitGroup)
		.inject(atOnceUsers(1)))
		.protocols(httpConf)
}
