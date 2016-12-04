lazy val commonDependencies = Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "joda-time" % "joda-time" % "2.9.6",
  "org.apache.spark" %% "spark-core" % "2.0.2",
  "org.apache.spark" %% "spark-sql" % "2.0.2",
  "com.chuusai" %% "shapeless" % "2.3.2",
"org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

val commonScalacOptions = Seq(
  "-Ywarn-dead-code",
  "-Ywarn-unused",
  "-Ywarn-unused-import"
)

lazy val commonSettings = Seq(
  organization := "fr.kayrnt",
  version := "0.0.1-SNAPSHOT",
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
  resolvers ++= Seq(Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"), Resolver.mavenLocal),
  javaOptions in run += "-Xmx6G",
  scalacOptions ++= commonScalacOptions,
  scalaVersion := "2.11.8"
)

def module(id: String) =
  Project(id, file(id))
    .settings(commonSettings: _*)
    .settings(libraryDependencies ++= commonDependencies)

lazy val core = module("core")

//Sample app
lazy val app = module("app")
  .dependsOn(core % "compile->compile;test->test")
  .settings(Revolver.settings ++
    Revolver.enableDebugging(port = 5005) ++
    Seq(javaOptions in reStart += "-Xmx6g",
      mainClass in reStart := Some("fr.kayrnt.app.KorailApp")))

lazy val output = module("output")

lazy val root = Project("Korail", file("."))
  .aggregate(core, output, app)