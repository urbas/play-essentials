import com.typesafe.sbt.pgp.PgpKeys._
import play.PlayImport._
import play._
import sbt.Keys._
import sbt._
import sbtrelease.ReleasePlugin
import sbtrelease.ReleasePlugin.ReleaseKeys._
import sbtrelease.ReleaseStateTransformations._
import si.urbas.sbtutils.docs._
import si.urbas.sbtutils.releases.ReleaseProcessTransformation
import si.urbas.sbtutils.textfiles.TextFileManipulation._
import si.urbas.sbtutils.textfiles._
import xerial.sbt.Sonatype
import xerial.sbt.Sonatype.SonatypeKeys
import xerial.sbt.Sonatype.SonatypeKeys._

object PlessBuild extends Build {

  val projectName = "Play Essentials"
  val projectUrl = "https://github.com/urbas/play-essentials"
  val projectScmUrl = "git@github.com:urbas/play-essentials.git"
  val ownerName = "urbas"
  val ownerUrl = "https://github.com/urbas"

  lazy val bumpPlessVersionsInReadmeMdFile = taskKey[Unit]("Replaces any references to the version of this project in the 'README.md' file.")

  override def settings: Seq[Def.Setting[_]] = {
    super.settings ++
      Seq(
        organization := "si.urbas",
        scalaVersion := "2.11.1",
        pomExtra := pomExtraSettings,
        sources in doc in Compile := Nil,
        // NOTE: We have to package documentation to conform to Sonatype's Repo policy
        publishArtifact in(Compile, packageDoc) := true,
        publishArtifact in(Compile, packageSrc) := true,
        publishArtifact in(Test, packageSrc) := false,
        credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
        publishMavenStyle := true,
        SonatypeKeys.profileName := "org.xerial",
        javacOptions += "-Xlint:unchecked"
      )
  }

  lazy val root = Project(
    id = "root",
    base = file("."),
    settings = rootProjectSettings,
    aggregate = Seq(pless, plessTest, plessJpa, plessJpaTest, plessJpaSample)
  )

  lazy val pless = Project(
    id = "pless",
    base = file("pless"),
    settings = plessProjectSettings
  )
    .enablePlugins(PlayJava)

  lazy val plessTest = Project(
      id = "plessTest",
      base = file("plessTest"),
      settings = plessTestProjectSettings,
      dependencies = Seq(pless)
    )
      .enablePlugins(PlayJava)

  lazy val plessJpa = Project(
      id = "plessJpa",
      base = file("plessJpa"),
      settings = plessJpaProjectSettings,
      dependencies = Seq(pless)
    )
      .enablePlugins(PlayJava)

  lazy val plessJpaTest = Project(
      id = "plessJpaTest",
      base = file("plessJpaTest"),
      settings = commonSettings("pless-jpa-test"),
      dependencies = Seq(pless, plessJpa, plessTest)
    )
      .enablePlugins(PlayJava)

  lazy val plessJpaSample = Project(
      id = "plessJpaSample",
      base = file("plessJpaSample"),
      settings = plessJpaSampleProjectSettings,
      dependencies = Seq(pless, plessJpa, plessTest, plessJpaTest)
    )
      .enablePlugins(PlayJava)

  private def commonSettings(projectName: String) = Seq(
    name := projectName
  ) ++
    Sonatype.sonatypeSettings ++
    si.urbas.sbtutils.textfiles.tasks ++
    si.urbas.sbtutils.docs.tasks

  private lazy val rootProjectSettings = {
    commonSettings("pless-root") ++
      ReleasePlugin.releaseSettings ++
      Seq(
        docsOutputDir := baseDirectory.value,
        readmeMdFile := baseDirectory.value / "README.md",
        bumpPlessVersionsInReadmeMdFile := {
          bumpVersionInFile(readmeMdFile.value, organization.value, "pless", version.value)
          bumpVersionInFile(readmeMdFile.value, organization.value, "pless-test", version.value)
        },
        releaseProcess := ReleaseProcessTransformation
          .insertTasks(bumpPlessVersionsInReadmeMdFile, generateAndStageDocs, addReadmeFileToVcs).after(setReleaseVersion)
          .replaceStep(publishArtifacts).withAggregatedTasks(publishSigned, sonatypeReleaseAll)
          .in(releaseProcess.value),
        docsSnippetDirs ++= {
          Seq("app/si/urbas/pless", "test/si/urbas/pless", "conf")
            .map(baseDirectory.in(plessJpaSample).value / _)
        }
      )
  }

  private lazy val plessProjectSettings = {
    commonSettings("pless") ++
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

  private lazy val plessJpaSampleProjectSettings = {
    commonSettings("pless-jpa-sample") ++
      Seq(docsOutputDir := baseDirectory.value / "..")
  }

  private lazy val pomExtraSettings = {
    <url>
      {projectUrl}
    </url>
      <licenses>
        <license>
          <name>Apache 2</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
        </license>
      </licenses>
      <scm>
        <connection>scm:git:
          {projectScmUrl}
        </connection>
        <developerConnection>scm:git:
          {projectScmUrl}
        </developerConnection>
        <url>
          {projectUrl}
        </url>
      </scm>
      <developers>
        <developer>
          <id>
            {ownerName}
          </id>
          <name>
            {ownerName}
          </name>
          <url>
            {ownerUrl}
          </url>
        </developer>
      </developers>
  }
}