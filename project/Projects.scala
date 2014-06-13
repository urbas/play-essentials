import com.typesafe.sbt.pgp.PgpKeys._
import play.PlayImport._
import play._
import sbt.Keys._
import sbt._
import sbtrelease.ReleasePlugin.ReleaseKeys._
import sbtrelease.ReleaseStateTransformations._
import si.urbas.sbtutils.docs._
import si.urbas.sbtutils.releases.ReleaseProcessTransformation
import si.urbas.sbtutils.textfiles.TextFileManipulation._
import si.urbas.sbtutils.textfiles._
import xerial.sbt.Sonatype.SonatypeKeys._

object Projects {

  def root(project: Project, aggregatedProjects: ProjectReference*): Project = {
    project.settings(rootProjectSettings: _*)
      .aggregate(aggregatedProjects: _*)
      .enablePlugins(PlayJava)
  }

  def pless(project: Project): Project = {
    project.settings(plessProjectSettings: _*)
      .enablePlugins(PlayJava)
  }

  def plessTest(project: Project, pless: Project): Project = {
    project.settings(plessTestProjectSettings: _*)
      .dependsOn(pless)
      .enablePlugins(PlayJava)
  }

  def plessJpa(project: Project, pless: Project): Project = {
    project.settings(plessJpaProjectSettings: _*)
      .dependsOn(pless)
      .enablePlugins(PlayJava)
  }

  def plessJpaTest(project: Project, pless: Project, plessJpa: Project, plessTest: Project): Project = {
    project.settings(plessJpaTestProjectSettings: _*)
      .dependsOn(pless, plessJpa, plessTest)
      .enablePlugins(PlayJava)
  }

  def plessJpaSample(project: Project, pless: Project, plessJpa: Project, plessTest: Project, plessJpaTest: Project): Project = {
    project.settings(plessJpaSampleProjectSettings: _*)
      .dependsOn(pless, plessJpa, plessTest, plessJpaTest)
      .enablePlugins(PlayJava)
  }

  private def commonSettings(projectName: String) = Seq(
    name := projectName
  )

  private lazy val rootProjectSettings = {
    commonSettings("pless-root") ++ ProjectSettings.rootSettings ++
      Seq(
        docsOutputDir := file("."),
        readmeMdFile := file("README.md"),
        ProjectSettings.bumpPlessVersionsInReadmeMdFile := {
          streams.value.log.error(s"What the fuck: ${readmeMdFile.value.getCanonicalPath}")
          bumpVersionInFile(readmeMdFile.value, organization.value, "pless", version.value)
          bumpVersionInFile(readmeMdFile.value, organization.value, "pless-test", version.value)
        },
        releaseProcess := ReleaseProcessTransformation
          .insertTasks(ProjectSettings.bumpPlessVersionsInReadmeMdFile, generateAndStageDocs, addReadmeFileToVcs).after(setReleaseVersion)
          .replaceStep(publishArtifacts).withAggregatedTasks(publishSigned, sonatypeReleaseAll)
          .in(releaseProcess.value)
      )
  }

  private lazy val plessProjectSettings = {
    commonSettings("pless") ++
      ProjectSettings.plessProjectWithPlaySettings ++
      Seq(
        libraryDependencies ++= Seq(
          cache,
          "commons-codec" % "commons-codec" % "1.9",
          "org.apache.commons" % "commons-email" % "1.3.2"
        )
      )
  }

  private lazy val plessTestProjectSettings = {
    commonSettings("pless-test") ++
      ProjectSettings.plessProjectWithPlaySettings ++
      Seq(
        libraryDependencies ++= Seq(
          "org.hamcrest" % "hamcrest-all" % "1.3",
          "org.hamcrest" % "hamcrest-core" % "1.3",
          ("junit" % "junit-dep" % "4.11")
            .exclude("org.hamcrest", "hamcrest-core"),
          ("org.mockito" % "mockito-all" % "1.9.5")
            .exclude("org.hamcrest", "hamcrest-core"),
          ("org.mockito" % "mockito-core" % "1.9.5")
            .exclude("org.hamcrest", "hamcrest-core"),
          "com.typesafe.play" %% "play-test" % play.core.PlayVersion.current
        )
      )
  }

  private lazy val plessJpaProjectSettings = {
    commonSettings("pless-jpa") ++
      ProjectSettings.plessProjectWithPlaySettings ++
      Seq(
        libraryDependencies ++= Seq(
          javaJdbc,
          javaJpa,
          "org.postgresql" % "postgresql" % "9.2-1004-jdbc4",
          "org.hibernate" % "hibernate-entitymanager" % "4.3.1.Final",
          "org.hibernate" % "hibernate-core" % "4.3.1.Final"
        )
      )
  }

  private lazy val plessJpaTestProjectSettings = {
    commonSettings("pless-jpa-test") ++ ProjectSettings.plessProjectWithPlaySettings
  }

  private lazy val plessJpaSampleProjectSettings = {
    commonSettings("pless-jpa-sample") ++ ProjectSettings.plessProjectWithPlaySettings ++
      Seq(
        docsOutputDir := baseDirectory.value / ".."
      )
  }
}