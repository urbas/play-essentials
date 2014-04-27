import com.typesafe.sbt.pgp.PgpKeys._
import de.johoop.jacoco4sbt.JacocoPlugin.jacoco
import ProjectInfo._
import sbt._
import sbt.Keys._
import sbtrelease.ReleasePlugin._
import sbtrelease.ReleasePlugin.ReleaseKeys._
import sbtrelease.ReleaseStateTransformations._
import si.urbas.sbtutils.releases.ReleaseProcessTransformation
import si.urbas.sbtutils.textfiles._
import xerial.sbt.Sonatype
import xerial.sbt.Sonatype.SonatypeKeys
import xerial.sbt.Sonatype.SonatypeKeys._

object ProjectSettings {
  lazy val apply: Seq[Setting[_]] = {
    Sonatype.sonatypeSettings ++
      jacoco.settings ++
      releaseSettings ++
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
        releaseProcess := ReleaseProcessTransformation
          .insertReleaseTasks(bumpVersionInReadmeMd, addReadmeFileToVcs).after(setReleaseVersion)
          .replaceReleaseStep(publishArtifacts).withTasks(publishSigned, sonatypeReleaseAll)
          .in(releaseProcess.value)
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

}