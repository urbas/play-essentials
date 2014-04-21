name := "pless-root"

organization := "si.urbas"

lazy val root = Project.project
  .in(file("."))
  .aggregate(pless)

lazy val pless = Project.project
  .in(file("pless"))

play.Project.playJavaSettings