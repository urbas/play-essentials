import de.johoop.jacoco4sbt._
import JacocoPlugin._

name := "pless"

organization := "si.urbas"

version := "0.0.2-SNAPSHOT"

publishTo := {
  val nexus = "http://urbas.si:8081/nexus/content/repositories"
  if (version.value.trim.endsWith("SNAPSHOT")) {
    Some("Snapshots" at s"$nexus/snapshots/")
  } else {
    Some("Releases" at s"$nexus/releases/")
  }
}

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

publishMavenStyle := true

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

sources in doc in Compile := Nil

publishArtifact in (Compile, packageDoc) := false

publishArtifact in (Compile, packageSrc) := true

publishArtifact in (Test, packageBin) := true

publishArtifact in (Test, packageSrc) := true

play.Project.playJavaSettings
