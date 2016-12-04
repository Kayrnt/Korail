package fr.kayrnt.korail

import org.scalatest.{Matchers, WordSpec}

import scala.collection.mutable

class SnowFlakeModelSpec extends WordSpec with Matchers with SnowFlakeModel {

  "SnowFlakeModel" should {

    "infer correct schema" in {
      val expected: Map[GenericDimension, DimensionNode] = Map(Ad -> DimensionNode(mutable.Buffer(),  mutable.Buffer(Insertion)))
      dimensionNodes should be(expected)

    }

  }

  override def getAllDimensions: List[GenericDimension] = List(Ad, Insertion, Placement, Website)
}

