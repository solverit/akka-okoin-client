name := "trade"

version := "1.0"

scalaVersion := "2.11.8"

lazy val commonSettings = Seq(
  organization := "com.awar.client",
  version := "1.0.0",
  scalaVersion := "2.11.8",

  assemblyJarName := "okoin-client.jar",

  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8", // 2 args
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-unchecked",
    "-Xfatal-warnings",
    //    "-Xlint",
    "-Yno-adapted-args",
    //    "-Ywarn-dead-code", // N.B. doesn't work well with the ??? hole
    //    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture",
    "-Ywarn-unused-import" // 2.11 only
  )
)

mainClass in (Compile, run) := Some("com.awar.Starter")

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

val akkaVersion = "2.4.4"

lazy val libDependencies = Seq(
  "org.scala-lang.modules" %% "scala-xml" % "1.0.4",

  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,

  "com.typesafe.slick" %% "slick" % "3.1.1",
  "com.github.tminglei" %% "slick-pg" % "0.11.0",
  "com.github.tminglei" %% "slick-pg_date2" % "0.11.0",

  "com.mchange" % "c3p0" % "0.9.5.2",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",

  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "ch.qos.logback" % "logback-core" % "1.1.2",

  "com.google.protobuf" % "protobuf-java" % "2.6.1"
)

lazy val testDependencies = Seq(
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)


lazy val root = project.in(file("."))
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= (libDependencies ++ testDependencies))

    