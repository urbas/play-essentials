import com.typesafe.sbt.pgp.PgpKeys.publishSigned
import de.johoop.jacoco4sbt.JacocoPlugin._
import sbtrelease.ReleasePlugin._
import sbtrelease.ReleasePlugin.ReleaseKeys._
import sbtrelease.ReleaseStateTransformations._
import si.urbas.sbtutils.releases.ReleaseProcessTransformation._
import si.urbas.sbtutils.textfiles._
import xerial.sbt.Sonatype
import xerial.sbt.Sonatype.SonatypeKeys
import xerial.sbt.Sonatype.SonatypeKeys._

name := "pless"

organization := "si.urbas"

Sonatype.sonatypeSettings

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

publishMavenStyle := true

SonatypeKeys.profileName := "org.xerial"

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
  ("junit" % "junit-dep" % "4.11" % "test")
    .exclude("org.hamcrest", "hamcrest-core"),
  ("org.mockito" % "mockito-all" % "1.9.5" % "test")
    .exclude("org.hamcrest", "hamcrest-core"),
  "org.hamcrest" % "hamcrest-all" % "1.3" % "test",
  "com.googlecode.catch-exception" % "catch-exception" % "1.2.0" % "test"
)

pomExtra := {
  <url>https://github.com/urbas/play-essentials</url>
    <licenses>
      <license>
        <name>Apache 2</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      </license>
    </licenses>
    <scm>
      <connection>scm:git:github.com/urbas/play-essentials</connection>
      <developerConnection>scm:git:git@github.com:urbas/play-essentials</developerConnection>
      <url>github.com/urbas/play-essentials</url>
    </scm>
    <developers>
      <developer>
        <id>urbas</id>
        <name>urbas</name>
        <url>https://github.com/urbas</url>
      </developer>
    </developers>
}

jacoco.settings

parallelExecution in jacoco.Config := false

sources in doc in Compile := Nil

publishArtifact in(Compile, packageDoc) := true

publishArtifact in(Compile, packageSrc) := true

sources in doc in Test := Nil

publishArtifact in(Test, packageBin) := true

publishArtifact in(Test, packageSrc) := true

releaseSettings

si.urbas.sbtutils.textfiles.tasks

releaseProcess := insertGlobalTasks(bumpVersionInReadmeMd).after(setReleaseVersion)
  .replaceReleaseStep(publishArtifacts).withGlobalTasks(publishSigned, sonatypeReleaseAll)
  .in(releaseProcess.value)

play.Project.playJavaSettings
