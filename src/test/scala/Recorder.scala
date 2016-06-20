import io.gatling.recorder.controller.RecorderController
import scala.collection.mutable

object Recorder extends App {

	RecorderController(mutable.Map(
		"outputFolder" -> Some(IDEPathHelper.recorderOutputDirectory.toString),
		"simulationPackage" -> Some("org.nuxeo.gatling.marklogic"),
		"requestBodiesFolder" -> Some(IDEPathHelper.requestBodiesDirectory.toString)))
}