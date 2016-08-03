package marklogic

import com.marklogic.xcc.ContentFactory
import io.gatling.core.Predef._
import org.nuxeo.gatling.marklogic.Predef._


class XCCUpdateSimulation extends Simulation {
	//val uri = "xcc://root:root@localhost:8020"
	//val uri = "xcc://admin:systemml@localhost:8100"
	val uri = "xcc://admin:systemml@localhost:18020"
	val xccConf = xcc.uri(uri)

	val docUri = "/03d0f22a-e1f7-4c1b-8cef-8ec3d89dd542.xml"
	var patchSpec = """
<document xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<ecm__racl>
		<diff>
			<ecm__racl__item xsi:type="xs:string">NOP</ecm__racl__item>
			<ecm__racl__item xsi:type="xs:string">gatling</ecm__racl__item>
			<ecm__racl__item xsi:type="xs:string">NULL</ecm__racl__item>
		</diff>
		<rpush>
			<ecm__racl__item xsi:type="xs:string">members</ecm__racl__item>
		</rpush>
	</ecm__racl>
	<new_element>1234</new_element>
</document>
	"""

	// XccMarkLogicSearchBuilder
	val search = xcc("update")
		.update(docUri, patchSpec)

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

	setUp(scenario("commit&search")
		.exec(commitGroup)
		.inject(atOnceUsers(20)))
		.protocols(xccConf)

	//val scn = scenario("cts:search")
	//  .exec(search)

	//setUp(
	//  scn.inject( constantUsersPerSec(10) during(3600) )
	//).protocols(xccConf)
}
