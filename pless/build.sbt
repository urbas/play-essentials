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
  "org.hamcrest" % "hamcrest-all" % "1.3" % "test",
  "org.hamcrest" % "hamcrest-core" % "1.3" % "test",
  ("junit" % "junit-dep" % "4.11" % "test")
    .exclude("org.hamcrest", "hamcrest-core"),
  ("org.mockito" % "mockito-all" % "1.9.5" % "test")
    .exclude("org.hamcrest", "hamcrest-core"),
  ("org.mockito" % "mockito-core" % "1.9.5" % "test")
    .exclude("org.hamcrest", "hamcrest-core")
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

javacOptions += "-Xlint:unchecked"

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

releaseProcess := insertGlobalTasks(bumpVersionInReadmeMd, addReadmeFileToVcs).after(setReleaseVersion)
  .replaceReleaseStep(publishArtifacts).withGlobalTasks(publishSigned, sonatypeReleaseAll)
  .in(releaseProcess.value)

//lazy val pless = Project.project
//  .in(file("."))
//  .aggregate(plessAggregator)
//
//lazy val plessAggregator = Project.project
//  .in(file("plessAggregator"))
//  .aggregate(plessTest, plessJpa, plessJpaTest)
//
//lazy val plessTest = Project.project
//  .in(file("plessTest"))
//  .dependsOn(pless % "compile->compile")
//
//lazy val plessJpa = Project.project
//  .in(file("plessJpa"))
//
//lazy val plessJpaTest = Project.project
//  .in(file("plessJpaTest"))

play.Project.playJavaSettings