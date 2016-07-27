package marklogic

import com.marklogic.xcc.ContentFactory
import io.gatling.core.Predef._
import org.nuxeo.gatling.marklogic.Predef._


class XCCGetSimulation extends Simulation {
	//val uri = "xcc://root:root@localhost:8020"
	//val uri = "xcc://admin:systemml@localhost:8100"
	val uri = "xcc://admin:systemml@localhost:18020"
	val xccConf = xcc.uri(uri)

	// XccMarkLogicGetBuilder
	val search = xcc("get")
		.uri("/07f02881-b305-45b2-8807-9e2d864dcf66.xml").get()

//  def scn(name: String) = scenario(name)
//  .exec(search)

//  setUp(
//    (for {
//      i <- 0 until 8
//     } yield {
//      scn(s"Test $i").inject(
//        atOnceUsers(1000)
//      )
//    }).toList // setUp can accept List[PopulationBuilder]
// ).protocols(xccConf)

	val commitGroup = during(40, "i") {
		randomSwitch(
			100.0 -> exec(search)
		)
	}

	setUp(scenario("get")
		.exec(commitGroup)
		.inject(atOnceUsers(20)))
		.protocols(xccConf)

	//val scn = scenario("cts:search")
	//  .exec(search)

	//setUp(
	//  scn.inject( constantUsersPerSec(10) during(3600) )
	//).protocols(xccConf)
}
