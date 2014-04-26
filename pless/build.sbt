import com.typesafe.sbt.pgp.PgpKeys.publishSigned
import sbtrelease.ReleasePlugin.ReleaseKeys._
import sbtrelease.ReleaseStateTransformations._
import si.urbas.sbtutils.releases.ReleaseProcessTransformation._
import si.urbas.sbtutils.textfiles._
import xerial.sbt.Sonatype.SonatypeKeys._

name := "pless"

organization := "si.urbas"

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

ProjectSettings.apply

releaseProcess := insertGlobalTasks(bumpVersionInReadmeMd, addReadmeFileToVcs).after(setReleaseVersion)
  .replaceReleaseStep(publishArtifacts).withGlobalTasks(publishSigned, sonatypeReleaseAll)
  .in(releaseProcess.value)

play.Project.playJavaSettings