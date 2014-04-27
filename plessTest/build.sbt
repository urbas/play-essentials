name := "pless-test"

organization := "si.urbas"

libraryDependencies ++= Seq(
  cache,
  "commons-codec" % "commons-codec" % "1.9",
  "org.apache.commons" % "commons-email" % "1.3.2"
)

// Test dependencies
libraryDependencies ++= Seq(
  "org.hamcrest" % "hamcrest-all" % "1.3",
  "org.hamcrest" % "hamcrest-core" % "1.3",
  ("junit" % "junit-dep" % "4.11")
    .exclude("org.hamcrest", "hamcrest-core"),
  ("org.mockito" % "mockito-all" % "1.9.5")
    .exclude("org.hamcrest", "hamcrest-core"),
  ("org.mockito" % "mockito-core" % "1.9.5")
    .exclude("org.hamcrest", "hamcrest-core")
)

ProjectSettings.apply

play.Project.playJavaSettings