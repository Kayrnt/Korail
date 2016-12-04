package fr.kayrnt.app

import fr.kayrnt.korail.{LongMetric, MetricMap}
import org.scalatest.{Matchers, WordSpec}
import shapeless._

/**
  * User: Kayrnt
  * Date: 03/12/2016
  * Time: 17:31
  */

class FactSpec extends WordSpec with Matchers {

  "Facts" should {

    "be definable" in {

      new TrackingFact(
        new TrackingKey(1L :: 1001L :: HNil),
        new MetricMap(Map("impression" -> LongMetric(10L)))
      )

      true should be(true)

    }

  }

}

