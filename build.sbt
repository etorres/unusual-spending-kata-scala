organization := "es.eriktorr.katas"
name := "unusual-spending-kata-scala"
version := "1.0"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.1.1" % Test,
  "org.scalacheck" %% "scalacheck" % "1.14.3" % Test
)

scalacOptions ++= Seq(
  "-encoding",
  "utf8",
  "-Xfatal-warnings",
  "-Xlint",
  "-deprecation",
  "-unchecked"
)

wartremoverErrors ++= Warts.unsafe
wartremoverWarnings ++= Warts.unsafe
wartremoverExcluded ++= PathFinder(baseDirectory.value / "src" / "test" / "scala")
  .**("*Spec.scala")
  .get

jacocoReportSettings := JacocoReportSettings().withThresholds(
  JacocoThresholds(
    instruction = 80,
    method = 100,
    branch = 100,
    complexity = 100,
    line = 90,
    clazz = 100
  )
)
