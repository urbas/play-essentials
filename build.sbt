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

lazy val root = Project.project
  .in(file("."))
  .aggregate(pless, plessTest, plessJpa, plessJpaTest, plessJpaSample)

lazy val pless = Project.project
  .in(file("pless"))

lazy val plessTest = Project.project
  .in(file("plessTest"))
  .dependsOn(pless % "compile->compile")

lazy val plessJpa = Project.project
  .in(file("plessJpa"))
  .dependsOn(pless % "compile->compile")

lazy val plessJpaTest = Project.project
  .in(file("plessJpaTest"))
  .dependsOn(pless % "compile->compile", plessJpa % "compile->compile", plessTest % "compile->compile")

lazy val plessJpaSample = Project.project
  .in(file("plessJpaSample"))
  .dependsOn(pless % "compile->compile", plessJpa % "compile->compile")

ReleasePlugin.releaseSettings ++ ProjectSettings.apply

docsOutputDir := file(".")

docsSnippetDirs += plessJpaSample.base / "app/si/urbas/pless"

releaseProcess := ReleaseProcessTransformation
  .insertTasks(bumpPlessVersionsInReadmeMdFile, generateAndStageDocs, addReadmeFileToVcs).after(setReleaseVersion)
  .replaceStep(publishArtifacts).withAggregatedTasks(publishSigned, sonatypeReleaseAll)
  .in(releaseProcess.value)

play.Project.playJavaSettings