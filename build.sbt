lazy val commonSettings = Seq(
  organization := "com.mojitoverde",
  version := "1.0-SNAPSHOT",
  scalaVersion := "2.11.7"
)
crossScalaVersions := Seq("2.11.7")

lazy val root = (project in file(".")).settings(commonSettings: _*).aggregate(util)

lazy val util = (project in file("modules/dbmodule")).settings(scalaVersion:="2.11.7",
  libraryDependencies ++= Seq(
    "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.4",
    "org.scala-lang.modules" % "scala-parser-combinators_2.11" % "1.0.4",
    "org.scala-lang" % "scala-reflect" % "2.11.7",
    "com.datastax.cassandra" % "cassandra-driver-core" % "3.1.1",
    "com.typesafe.play" %% "play" % "2.3.10",
    "com.google.code.findbugs" % "jsr305" % "1.3.9"
  )
)

//"com.google.code.findbugs" % "jsr305" % "1.3.9" => for add jmx.annotations
scalaVersion in ThisBuild := "2.11.7"
scalacOptions += "-feature"