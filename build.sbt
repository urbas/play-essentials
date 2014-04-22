name := "pless-root"

organization := "si.urbas"

lazy val plessRoot = Project.project
  .in(file("."))
  .aggregate(pless, plessTest)

lazy val pless = Project.project
  .in(file("pless"))

lazy val plessTest = Project.project
  .in(file("plessTest"))
  .dependsOn(pless % "compile->compile")

play.Project.playJavaSettings