package fr.kayrnt.app

import fr.kayrnt.korail._
import shapeless._

/**
  * User: Kayrnt
  * Date: 04/12/2016
  * Time: 16:27
  */

class TrackingKey(keys: Insertion.Key :: Placement.Key :: HNil)
  extends DimensionKeyList(keys)

class TrackingFact(override val key: TrackingKey, override val metrics: MetricMap)
  extends Fact[TrackingKey, MetricMap]