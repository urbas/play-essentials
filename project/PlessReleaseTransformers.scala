import sbtrelease.ReleaseStep

object PlessReleaseTransformers {
  def insertReleaseSteps(steps: ReleaseStep*): ReleaseStepInsertion = {
    ReleaseStepInsertion(steps:_*)
  }
}

case class ReleaseStepInsertion(steps: ReleaseStep*) {
  def into(releaseProcess: Seq[ReleaseStep]): ReleaseStepInsertionWithReleaseProcess = {
    ReleaseStepInsertionWithReleaseProcess(this, releaseProcess)
  }
}

case class ReleaseStepInsertionWithReleaseProcess(insertion: ReleaseStepInsertion, releaseProcess: Seq[ReleaseStep]) {
  def before(step: ReleaseStep): Seq[ReleaseStep] = {
    val (stepsBefore, stepsAfter) = releaseProcess.partition(IsStepBeforeFilter(step))
    stepsBefore ++ insertion.steps ++ stepsAfter
  }
}

case class IsStepBeforeFilter(targetStep: ReleaseStep) extends (ReleaseStep => Boolean) {
  var wasSetVersionStepFound = false

  override def apply(step: ReleaseStep): Boolean = {
    if (!wasSetVersionStepFound) {
      wasSetVersionStepFound = step == targetStep
    }
    !wasSetVersionStepFound
  }
}