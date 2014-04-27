import de.johoop.jacoco4sbt.JacocoPlugin.jacoco
import ProjectInfo._
import sbt._
import sbt.Keys._
import si.urbas.sbtutils.textfiles.TextFileManipulation._
import xerial.sbt.Sonatype
import xerial.sbt.Sonatype.SonatypeKeys

object ProjectSettings {

  lazy val bumpPlessVersionsInReadmeMdFile = taskKey[Unit]("Replaces any references to the version of this project in 'project/plugins.sbt'.")

  lazy val apply: Seq[Setting[_]] = {
    Sonatype.sonatypeSettings ++
      jacoco.settings ++
      Seq(
        pomExtra := pomExtraSettings,
        sources in doc in Compile := Nil,
        publishArtifact in(Compile, packageDoc) := false,
        publishArtifact in(Compile, packageSrc) := true,
        publishArtifact in(Test, packageSrc) := false,
        parallelExecution in jacoco.Config := false,
        credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
        publishMavenStyle := true,
        SonatypeKeys.profileName := "org.xerial",
        javacOptions += "-Xlint:unchecked",
        bumpPlessVersionsInReadmeMdFile := {
          bumpVersionInFile(readmeMdFile, organization.value, "pless", version.value)
          bumpVersionInFile(readmeMdFile, organization.value, "pless-test", version.value)
        }
      ) ++
      si.urbas.sbtutils.textfiles.tasks
  }

  val pomExtraSettings = {
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

  lazy val readmeMdFile = file("README.md")

}