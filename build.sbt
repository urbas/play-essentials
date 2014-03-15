import de.johoop.jacoco4sbt._
import JacocoPlugin._

name := "Play Essentials"

organization := "com.urmest"

version := "0.0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa,
  cache,
  "org.postgresql" % "postgresql" % "9.2-1004-jdbc4",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.1.Final",
  "org.hibernate" % "hibernate-core" % "4.3.1.Final",
  "commons-codec" % "commons-codec" % "1.9",
  "org.apache.commons" % "commons-email" % "1.3.2"
)

// Test dependencies
libraryDependencies ++= Seq(
  "org.mockito" % "mockito-all" % "1.9.5" % "test",
  "org.hamcrest" % "hamcrest-all" % "1.3" % "test",
  "com.googlecode.catch-exception" % "catch-exception" % "1.2.0" % "test"
)

jacoco.settings

parallelExecution in jacoco.Config := false

publishArtifact in (Test, packageBin) := true

play.Project.playJavaSettings