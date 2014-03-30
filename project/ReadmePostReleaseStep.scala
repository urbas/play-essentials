import sbtrelease.ReleasePlugin.ReleaseKeys.versions
import sbtrelease.ReleaseStep
import sbt._

object ReadmePostReleaseStep {

  def processReadmeFile: ReleaseStep = {
    ReleaseStep(
      action = state => {
        val readmeFile = file("README.md")
        val transformedContent = IO.read(readmeFile).replaceAll(
          "(\"si\\.urbas\" %% \"pless\" %) \"([^\"]+)\"",
          "$1 \"" + state.get(versions).get._1 + "\""
        )
        IO.write(readmeFile, transformedContent)
        state
      },
      check = identity
    )
  }

}