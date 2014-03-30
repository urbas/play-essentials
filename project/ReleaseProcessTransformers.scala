import sbtrelease.ReleaseStep

object ReleaseProcessTransformers {
  def insertReleaseStep(step: ReleaseStep): ReleaseStepInsertee = {
    ReleaseStepInsertee(step)
  }
}

case class ReleaseStepInsertee(step: ReleaseStep) {
  def into(releaseProcess: Seq[ReleaseStep]): ReleaseStepInserteeWithReleaseProcess = {
    ReleaseStepInserteeWithReleaseProcess(this, releaseProcess)
  }
}

case class ReleaseStepInserteeWithReleaseProcess(insertee: ReleaseStepInsertee, releaseProcess: Seq[ReleaseStep]) {
  def before(step: ReleaseStep): Seq[ReleaseStep] = {
    val (stepsBefore, stepsAfter) = releaseProcess.partition(IsStepBeforeFilter(step))
    stepsBefore ++
      Seq(insertee.step) // ++ stepsAfter
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