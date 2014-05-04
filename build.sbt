import ProjectSettings.bumpPlessVersionsInReadmeMdFile
import com.typesafe.sbt.pgp.PgpKeys._
import sbtrelease.ReleasePlugin
import sbtrelease.ReleasePlugin.ReleaseKeys._
import sbtrelease.ReleaseStateTransformations._
import si.urbas.sbtutils.docs._
import si.urbas.sbtutils.releases.ReleaseProcessTransformation
import si.urbas.sbtutils.textfiles._
import xerial.sbt.Sonatype.SonatypeKeys._

name := "pless-root"

lazy val root = project.in(file("."))
  .aggregate(pless, plessTest, plessJpa, plessJpaTest, plessJpaSample)

lazy val pless = project

lazy val plessTest = project.dependsOn(pless)

lazy val plessJpa = project.dependsOn(pless)

lazy val plessJpaTest = project.dependsOn(pless, plessJpa, plessTest)

lazy val plessJpaSample = project.dependsOn(pless, plessJpa, plessTest, plessJpaTest)

ReleasePlugin.releaseSettings ++ ProjectSettings.apply

docsOutputDir := file(".")

docsSnippetDirs ++= Seq("app/si/urbas/pless", "test/si/urbas/pless", "conf").map(plessJpaSample.base / _)

releaseProcess := ReleaseProcessTransformation
  .insertTasks(bumpPlessVersionsInReadmeMdFile, generateAndStageDocs, addReadmeFileToVcs).after(setReleaseVersion)
  .replaceStep(publishArtifacts).withAggregatedTasks(publishSigned, sonatypeReleaseAll)
  .in(releaseProcess.value)

play.Project.playJavaSettings