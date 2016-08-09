package marklogic

import com.marklogic.xcc.ContentFactory
import io.gatling.core.Predef._
import org.nuxeo.gatling.marklogic.Predef._


class XCCPutSearchSimulation extends Simulation {
	val threads = 8
	val docsPerThread = 20000

  val uri = "xcc://admin:systemml@localhost:18020"
  //val uri = "xcc://admin:systemml@localhost:8100"
  //val uri = "xcc://admin:systemml@ip-10-93-157-85:8020"
  val xccConf = xcc.uri(uri)

  val mlContent = ContentFactory.newContent("/${docId}.xml", """<document xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><ecm__name xsi:type="xs:string">doc</ecm__name><ecm__racl><nxml__item xsi:type="xs:string">Administrator</nxml__item><nxml__item xsi:type="xs:string">administrators</nxml__item><nxml__item xsi:type="xs:string">members</nxml__item></ecm__racl><ecm__primaryType xsi:type="xs:string">MyDocType</ecm__primaryType><my__testDefault xsi:type="xs:string">the default value</my__testDefault><my__testDefaultLong xsi:type="xs:long">0</my__testDefaultLong><ecm__id xsi:type="xs:string">${docId}</ecm__id><ecm__parentId xsi:type="xs:string">00000000-0000-0000-0000-000000000000</ecm__parentId><ecm__ancestorIds><nxml__item xsi:type="xs:string">00000000-0000-0000-0000-000000000000</nxml__item></ecm__ancestorIds><my__string xsi:type="xs:string">foo</my__string></document>""", null)

  val random = new util.Random
  val feeder = Iterator.continually(Map("docId" -> random.nextLong()))
  val insert = xcc("insert")
    .uri("/${docId}.xml")
    .insert("""<document xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><ecm__name xsi:type="xs:string">doc</ecm__name><ecm__racl><nxml__item xsi:type="xs:string">Administrator</nxml__item><nxml__item xsi:type="xs:string">administrators</nxml__item><nxml__item xsi:type="xs:string">members</nxml__item></ecm__racl><ecm__primaryType xsi:type="xs:string">MyDocType</ecm__primaryType><my__testDefault xsi:type="xs:string">the default value</my__testDefault><my__testDefaultLong xsi:type="xs:long">0</my__testDefaultLong><ecm__id xsi:type="xs:string">${docId}</ecm__id><ecm__parentId xsi:type="xs:string">00000000-0000-0000-0000-000000000000</ecm__parentId><ecm__ancestorIds><nxml__item xsi:type="xs:string">00000000-0000-0000-0000-000000000000</nxml__item></ecm__ancestorIds><my__string xsi:type="xs:string">foo</my__string></document>""")

  val search = xcc("search")
    .findOne(
    	"ecm__parentId", "a68687ef-558c-4990-a8ad-5eebb5b9b778",
    	"ecm__name", "Task2225",
    	Array(
        "350d065f-771a-45d5-8395-40ef69f8b22a",
        "c41ee577-8834-49a6-a6d8-31b0fec28c3e",
        "c7e844b1-d68e-4385-aa6a-e13edef1697d",
        "a68687ef-558c-4990-a8ad-5eebb5b9b778",
        "215c00e0-4e9b-4f4e-abfe-4c25a9f0d38c",
        "11e71265-ddc4-4744-83e6-cc1a3235aedd",
        "061afda4-5f99-4206-9807-3c1ef84c0c69",
        "00000000-0000-0000-0000-000000000000",
        "ef5c4c2b-8909-45ca-9e9d-1b929fe9fb71",
        "d1f13921-5e1b-4663-a7fd-17ad638a2d97"
      )
    )

	val get = xcc("get")
		.uri("/07f02881-b305-45b2-8807-9e2d864dcf66.xml").get()

	def scn(name: String) = scenario(name)
	    .randomSwitch(
	    	33.3 -> exec(get),
	      33.3 -> exec(search),
	      33.3 -> feed(feeder).exec(insert)
	    )

  setUp(
	  (for {
	    i <- 0 until threads
	   } yield {
	    scn(s"Test $i")
	    	.inject(atOnceUsers(docsPerThread))
	  }).toList // setUp can accept List[PopulationBuilder]
  ).protocols(xccConf)

}
