package fr.kayrnt.app

import fr.kayrnt.korail.{Dimension, DataSource, HasDataSource}
import org.joda.time.DateTime

/**
  * User: Kayrnt
  * Date: 04/12/2016
  * Time: 16:26
  */
object Insertion extends Dimension[Long] with HasDataSource {
  val name = DimensionProperty[String]("name")
  val creationDate = DimensionProperty[DateTime]("creation_date")

  override def properties: List[DimensionProperty[_]] =
    List(
      id,
      name,
      creationDate)

  //mock
  override def getPropertiesValues(properties: List[DimensionProperty[_]],
                                   modelIds: List[Long])
  : Map[DimensionProperty[_], Any] =
    Map(id -> 1L, name -> "testAd")

  override def datasources: List[DataSource] = List(BQDailyDataSource, BQHourlyDataSource)
}

object Ad extends Dimension[Long] {

  val name = DimensionProperty[String]("name")
  val budget = DimensionProperty[Long]("budget")
  val creationDate = DimensionProperty[DateTime]("creation_date")

  override def properties: List[DimensionProperty[_]] =
    List(
      id,
      name,
      budget,
      creationDate
    )

  override def referencedBy: List[DimensionForeignKey[_]] =
    List(DimensionForeignKey(Insertion, "ad_id"))

  //mock
  override def getPropertiesValues(properties: List[DimensionProperty[_]],
                                   modelIds: List[Long]):
  Map[DimensionProperty[_], Any] =
    Map(id -> 1L, name -> "testAd", budget -> 1000L)

}

object Placement extends Dimension[Long] with HasDataSource {

  val name = DimensionProperty[String]("name")
  val creationDate = DimensionProperty[DateTime]("creation_date")

  override def properties: List[DimensionProperty[_]] =
    List(
      name,
      creationDate)

  override def datasources: List[DataSource] = List(BQDailyDataSource, BQHourlyDataSource)

}

object Website extends Dimension[Long]