import com.typesafe.sbt.pgp.PgpKeys
import sbt.Keys._
import sbtrelease.ReleasePlugin.ReleaseKeys
import sbtrelease.ReleasePlugin.ReleaseKeys.versions
import sbtrelease.ReleaseStep
import sbt._
import xerial.sbt.Sonatype.SonatypeKeys

object PlessReleaseSteps {

  lazy val bumpVersionInReadmeFile: ReleaseStep = replaceTextInFile(
    readmeMdFile,
    regexPattern = "(\"si\\.urbas\" %% \"pless\" %) \"([^\"]+)\"",
    replacementPattern = state => "$1 \"" + state.get(versions).get._1 + "\""
  )

  lazy val addReadmeFileToVcs: ReleaseStep = addFileToVcs(readmeMdFile)

  lazy val readmeMdFile: sbt.File = {
    file("README.md")
  }

  lazy val publishSigned = ReleaseStep(
    action = state => {
      val project = Project.extract(state)
      val ref = project.get(thisProjectRef)
      project.runAggregated(PgpKeys.publishSigned in Global in ref, state)
    }
  )

  lazy val sonatypeRelease = ReleaseStep(
    action = state => {
      val project = Project.extract(state)
      val ref = project.get(thisProjectRef)
      project.runAggregated(SonatypeKeys.sonatypeReleaseAll in Global in ref, state)
    }
  )

  def replaceTextInFile(file: sbt.File, regexPattern: String, replacementPattern: State => String): ReleaseStep = {
    ReleaseStep(
      action = state => {
        val transformedContent = IO.read(file).replaceAll(
          regexPattern,
          replacementPattern(state)
        )
        IO.write(readmeMdFile, transformedContent)
        state
      },
      check = identity
    )
  }

  def addFileToVcs(file: sbt.File): ReleaseStep = {
    ReleaseStep(
      action = state => {
        val vcs = Project.extract(state).get(ReleaseKeys.versionControlSystem).get
        val base = vcs.baseDir
        val relativePath = IO.relativize(base, file).get
        vcs.add(relativePath)
        state
      },
      check = identity
    )
  }
}