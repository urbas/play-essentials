import ProjectInfo._
import sbt.Keys._
import sbt._
import sbtrelease.ReleasePlugin
import xerial.sbt.Sonatype
import xerial.sbt.Sonatype.SonatypeKeys

object ProjectSettings {

  lazy val bumpPlessVersionsInReadmeMdFile = taskKey[Unit]("Replaces any references to the version of this project in the 'README.md' file.")

  lazy val plessCommonSettings = {
    Seq(organization := "si.urbas") ++
      Sonatype.sonatypeSettings ++
      Seq(
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
      ) ++
      si.urbas.sbtutils.textfiles.tasks ++
      si.urbas.sbtutils.docs.tasks
  }

  lazy val plessProjectWithPlaySettings = {
    ProjectSettings.plessCommonSettings
  }

  lazy val rootSettings = {
    ReleasePlugin.releaseSettings ++ ProjectSettings.plessCommonSettings
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

}