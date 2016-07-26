package marklogic

import com.marklogic.xcc.ContentFactory
import io.gatling.core.Predef._
import org.nuxeo.gatling.marklogic.Predef._


class XCCSearchSearchSimulation extends Simulation {
  //val uri = "xcc://root:root@localhost:8020"
  val uri = "xcc://admin:systemml@localhost:18020"
  val xccConf = xcc.uri(uri)


  val random = new util.Random
  val feeder = Iterator.continually(Map("docId" -> random.nextLong()))

  val search = xcc("search")
    .search("""
xquery version "1.0-ml";
import module namespace search = "http://marklogic.com/appservices/search"
                    at "/MarkLogic/appservices/search/search.xqy";

let $query :=
<query xmlns="http://marklogic.com/appservices/search" xmlns:search="http://marklogic.com/appservices/search" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <value-query type="string">
    <element ns="" name="ecm__parentId"/>
    <text>a68687ef-558c-4990-a8ad-5eebb5b9b778</text>
  </value-query>
  <value-query type="string">
    <element ns="" name="ecm__name"/>
    <text>Task2225</text>
  </value-query>
  <not-query>
    <value-query type="string">
      <element ns="" name="ecm__id"/>
      <text>350d065f-771a-45d5-8395-40ef69f8b22a</text>
      <text>c41ee577-8834-49a6-a6d8-31b0fec28c3e</text>
      <text>c7e844b1-d68e-4385-aa6a-e13edef1697d</text>
      <text>a68687ef-558c-4990-a8ad-5eebb5b9b778</text>
      <text>215c00e0-4e9b-4f4e-abfe-4c25a9f0d38c</text>
      <text>11e71265-ddc4-4744-83e6-cc1a3235aedd</text>
      <text>061afda4-5f99-4206-9807-3c1ef84c0c69</text>
      <text>00000000-0000-0000-0000-000000000000</text>
      <text>ef5c4c2b-8909-45ca-9e9d-1b929fe9fb71</text>
      <text>d1f13921-5e1b-4663-a7fd-17ad638a2d97</text>
    </value-query>
  </not-query>
</query>
let $options :=
<options xmlns="http://marklogic.com/appservices/search">
  <debug>false</debug>
  <transform-results apply="raw"/>
  <return-metrics>false</return-metrics>
  <return-qtext>false</return-qtext>
  <return-results>true</return-results>
  <return-facets>false</return-facets>
  <search-option>unfiltered</search-option>
</options>

return search:resolve($query, $options)
    	""")

  setUp(scenario("search:search")
    .during(40, "i") {
      randomSwitch(
        50.0 -> exec(search),
        50.0 -> exec(search)
      )
    }
    .inject(atOnceUsers(20)))
    .protocols(xccConf)
}
