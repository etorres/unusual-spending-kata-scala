organization := "es.eriktorr.katas"
name := "unusual-spending-scala"
version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.1.1" % Test
)

scalacOptions ++= Seq(
  "-encoding", "utf8",
  "-Xfatal-warnings",
  "-Xlint",
  "-deprecation",
  "-unchecked"
)

wartremoverErrors ++= Warts.unsafe
wartremoverWarnings ++= Warts.unsafe

jacocoReportSettings := JacocoReportSettings().withThresholds(
  JacocoThresholds(
    instruction = 80,
    method = 100,
    branch = 100,
    complexity = 100,
    line = 90,
    clazz = 100)
)
