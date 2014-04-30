import si.urbas.sbtutils.docs.docsOutputDir

name := "pless-jpa-sample"

libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa,
  cache
)

ProjectSettings.apply

docsOutputDir := baseDirectory.value / ".."

play.Project.playJavaSettings