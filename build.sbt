lazy val commonSettings = Seq(
  name:= """scala-cassandra""",
  organization := "com.mojitoverde",
  version := "1.0-SNAPSHOT",
  scalaVersion := "2.11.7"
)

/**
  *  Aggregation means that running a task on the aggregate project will also run it on the aggregated projects.
  *
  *  the root project aggregates estfulapi,util and daemons. Start up sbt with two subprojects
  *  as in the example, and try compile.
  *
  */
lazy val root = (project in file(".")).aggregate(cassandra).
  settings(commonSettings: _*)

scalacOptions ++= Seq("-feature")

lazy val scalatest = "org.scalatest" %% "scalatest" % "3.0.1"

// "com.google.code.findbugs" % "jsr305" % "1.3.9" solve warn detailed underneath

// [warn] Class javax.annotation.Nullable not found - continuing with a stub.
// [warn] Class javax.annotation.Nullable not found - continuing with a stub.

lazy val cassandra = (project in file("modules/cassandra")).settings(scalaVersion:="2.11.7",
  libraryDependencies ++= Seq(
    "com.datastax.cassandra" % "cassandra-driver-core" % "3.2.0",
    "com.typesafe.play" % "play-json_2.11" % "2.5.10",
    "com.google.code.findbugs" % "jsr305" % "1.3.9",
    scalatest % "test"
  )
)

