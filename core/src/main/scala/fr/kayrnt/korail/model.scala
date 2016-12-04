package fr.kayrnt.korail

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.joda.time.{DateTime, DateTimeZone}

import shapeless._

import scala.collection.mutable

/**
  * User: Kayrnt
  * Date: 03/12/2016
  * Time: 17:17
  */


trait SnowFlakeModel {

  type GenericDimension = Dimension[_]

  case class DimensionNode(parents: mutable.Buffer[GenericDimension] = mutable.Buffer(),
                           children: mutable.Buffer[GenericDimension] = mutable.Buffer())

  val dimensionNodes: Map[GenericDimension, DimensionNode] =
    getAllDimensions.foldLeft[Map[GenericDimension, DimensionNode]](Map()){
      case (currentMap, dimension) =>
        val mutableMap = new mutable.HashMap() ++ currentMap
        val children: mutable.Buffer[GenericDimension] = mutable.Buffer()
        dimension.referencedBy.map {
        childDimension =>
          val safeChildDim = childDimension.dimension.asInstanceOf[GenericDimension]
          children += safeChildDim
          mutableMap.get(safeChildDim) match {
            case None => mutableMap + (safeChildDim -> DimensionNode(parents = mutable.Buffer() += dimension))
            case Some(node) => node.parents += safeChildDim
          }
      }
        mutableMap.get(dimension) match {
          case None => mutableMap + (dimension -> DimensionNode(children = children))
          case Some(node) => node.children ++= children
        }
        mutableMap.toMap
    }

  def getAllDimensions: List[GenericDimension]

}

trait BaseDimensionProperty[T, D] {
  def name: String
  type ValueType = T
  type Dimension = D
}

trait Dimension[K] {

  case class DimensionProperty[T](name: String)
    extends BaseDimensionProperty[T, this.type]

  type Key = K

  def id = DimensionProperty[K]("id")

  case class DimensionForeignKey[D <: Dimension[_]](dimension: D, fieldName: String)

  def properties: List[DimensionProperty[_]] = Nil

  def referencedBy: List[DimensionForeignKey[_]] = Nil

  def getPropertiesValues(properties: List[DimensionProperty[_]],
                          modelIds: List[K])
  : Map[DimensionProperty[_], Any] = Map()

  def dimensionName: String = this.getClass.getSimpleName.toLowerCase

}

trait DataSource {

  def getDataSourceAvailabilities(requestReportingPeriod: ReportingPeriod): List[ReportingPeriod] = Nil

  def getFacts(ctx: BaseRequestContext, period: ReportingPeriod): RDD[Fact[_, _]] = ctx.sc.emptyRDD

}

trait HasDataSource {

  def datasources: List[DataSource]

}

trait ReferenceKeys

trait Metrics

trait Metric[T <: AnyVal] {
  def value: T
}

trait Fact[K <: ReferenceKeys, M <: Metrics] {
  def key: K

  def metrics: M
}

case class ReportingPeriod(start: DateTime, end: DateTime, zone: DateTimeZone = DateTimeZone.UTC)

//implementations

case class LongMetric(override val value: Long) extends Metric[Long]

class DimensionKeyList[K <: HList](val dimensions: K) extends ReferenceKeys

class MetricMap(val metricMap: Map[String, Metric[_]]) extends Metrics

trait BaseRequestContext {
  def sc: SparkContext
}
case class RequestContext(sc: SparkContext, dimensionProperties: List[String], metrics: List[String], period: ReportingPeriod) extends BaseRequestContext
