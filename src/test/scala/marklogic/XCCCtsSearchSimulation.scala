package marklogic

import com.marklogic.xcc.ContentFactory
import io.gatling.core.Predef._
import org.nuxeo.gatling.marklogic.Predef._


class XCCCtsSearchSimulation extends Simulation {
  //val uri = "xcc://root:root@localhost:8020"
  //val uri = "xcc://admin:systemml@localhost:8100"
  val uri = "xcc://admin:systemml@localhost:18020"
  val xccConf = xcc.uri(uri)

  val search = xcc("search")
    .search("""
cts:search(
  fn:collection(),
  cts:and-query((
    cts:element-value-query(fn:QName("","ecm__parentId"), "a68687ef-558c-4990-a8ad-5eebb5b9b778", ("lang=en"), 1),
    cts:element-value-query(fn:QName("","ecm__name"), "Task2225", ("lang=en"), 1),
    cts:not-query(
      cts:element-value-query(fn:QName("","ecm__id"), (
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
      ))
    )
  ))
)
""")

  setUp(scenario("cts:search")
    .during(40, "i") {
      randomSwitch(
        100.0 -> exec(search)
      )
    }
    .inject(atOnceUsers(20)))
    .protocols(xccConf)
}
