name := "simple-text-search"

version := "0.1"

scalaVersion := "2.13.1"

scalacOptions ++= Seq("-feature", "-deprecation", "-Xfatal-warnings", "-unchecked")

libraryDependencies ++= Seq(
  "org.scalamock" %% "scalamock" % "4.4.0" % Test,
  "org.scalatest" %% "scalatest" % "3.0.8" % Test
)
