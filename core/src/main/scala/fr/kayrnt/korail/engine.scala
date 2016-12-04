package fr.kayrnt.korail

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
  * User: Kayrnt
  * Date: 04/12/2016
  * Time: 17:02
  */

object Engine {


  def requestReporting(dimensionProperties: List[String],
                       metrics: List[String], period: ReportingPeriod)
  : RDD[Map[String, String]] = {

    val requestContext = RequestContext(new SparkContext(), dimensionProperties, metrics, period)
    val typedDimensionProperties: List[BaseDimensionProperty[_,_]] = extractDimensionProperties(dimensionProperties)
    val dataSourcesValidForRequest = getDataSourcesValidForRequest(typedDimensionProperties, metrics)
    val dataSourcesAvailableOnPeriods = getDataSourcesAvailableOnPeriod(period, dataSourcesValidForRequest)
    val facts = getFactsForRequest(dataSourcesAvailableOnPeriods, requestContext)
    requestContext.sc.emptyRDD
  }

  def extractDimensionProperties(dimensionProperties: List[String]): List[BaseDimensionProperty[_, _]] = ???
  def getDataSourcesValidForRequest(typedDimensionProperties: List[BaseDimensionProperty[_, _]], metrics: List[String]): List[DataSource] = ???
  def getDataSourcesAvailableOnPeriod(period: ReportingPeriod, dataSourcesValidForRequest: List[DataSource]): Map[DataSource, List[ReportingPeriod]]  = ???
  def getFactsForRequest(datasourcesWithPeriods: Map[DataSource, List[ReportingPeriod]], ctx: BaseRequestContext): RDD[Fact[_, _]] =
    datasourcesWithPeriods.flatMap {
    case (datasource, periods) =>
      periods.map {
        period => datasource.getFacts(ctx, period)
      }
  }.reduceLeft(_.union(_))

}