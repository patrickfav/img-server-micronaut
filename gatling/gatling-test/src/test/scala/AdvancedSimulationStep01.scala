import scala.util.Random

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class AdvancedSimulationStep01 extends Simulation {

  object IdFeeder {
    val feeder = Iterator.continually(Map("id" -> Math.abs(Random.nextLong)))
  }

  object AgeFeeder {
    val feeder = Iterator.continually(Map("age" -> Random.nextInt(99)))
  }

  object NameFeeder{
    val feeder = Iterator.continually(Map("name" -> (Random.alphanumeric.take(20).mkString)))
  }


  object CreatePerson {
    val createPerson =
      exec(http("Check persons") // let's give proper names, as they are displayed in the reports
        .get("/person/"))
        .pause(2,7)
        .exec(http("Add new person")
          .post("/person")
          .header("Content-Type", "application/json")
          .body(StringBody("""{"id":${id},"name":"${name}","age":${age}}""")))
        .pause(1,3)
        .exec(http("Get created Person").get("/person/${id}"))
        .pause(0,5)
  }

  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("text/html,application/json")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  // Now, we can write the scenario as a composition
  val scn = scenario("Simple Test").feed(IdFeeder.feeder).feed(NameFeeder.feeder).feed(AgeFeeder.feeder).exec(CreatePerson.createPerson)

  setUp(scn.inject(rampUsers(10000) during (100 seconds)).protocols(httpProtocol))
}

