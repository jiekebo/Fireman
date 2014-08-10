name := "Fireman"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.0"

libraryDependencies ++= Seq(
  "com.github.sstone" % "amqp-client_2.11" % "1.4",
  "com.typesafe.akka" % "akka-actor_2.11" % "2.3.4"
)