name := "pless"

libraryDependencies ++= Seq(
  cache,
  "commons-codec" % "commons-codec" % "1.9",
  "org.apache.commons" % "commons-email" % "1.3.2"
)

ProjectSettings.apply

play.Project.playJavaSettings