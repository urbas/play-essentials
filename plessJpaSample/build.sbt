import si.urbas.sbtutils.docs.docsOutputDir

name := "pless-jpa-sample"

libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa,
  cache
)

ProjectSettings.plessProjectWithPlaySettings

docsOutputDir := baseDirectory.value / ".."