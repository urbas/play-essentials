import com.typesafe.sbt.pgp.PgpKeys._
import sbtrelease.ReleasePlugin
import sbtrelease.ReleasePlugin.ReleaseKeys._
import sbtrelease.ReleaseStateTransformations._
import si.urbas.sbtutils.releases.ReleaseProcessTransformation
import si.urbas.sbtutils.textfiles._
import xerial.sbt.Sonatype.SonatypeKeys._

name := "pless-root"

organization := "si.urbas"

lazy val root = Project.project
  .in(file("."))
  .aggregate(pless, plessTest)

lazy val pless = Project.project
  .in(file("pless"))

lazy val plessTest = Project.project
  .in(file("plessTest"))
  .dependsOn(pless % "compile->compile")

ReleasePlugin.releaseSettings ++ ProjectSettings.apply

releaseProcess := ReleaseProcessTransformation
  .insertReleaseTasks(bumpVersionInReadmeMd, addReadmeFileToVcs).after(setReleaseVersion)
  .replaceReleaseStep(publishArtifacts).withTasks(publishSigned, sonatypeReleaseAll)
  .in(releaseProcess.value)

play.Project.playJavaSettings