import de.johoop.jacoco4sbt.JacocoPlugin._

name := "pless-test"

organization := "si.urbas"

libraryDependencies ++= Seq(
  cache,
  "commons-codec" % "commons-codec" % "1.9",
  "org.apache.commons" % "commons-email" % "1.3.2",
  "si.urbas" %% "pless" % version.value
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

javacOptions += "-Xlint:unchecked"

jacoco.settings

parallelExecution in jacoco.Config := false

sources in doc in Compile := Nil

publishArtifact in(Compile, packageDoc) := true

publishArtifact in(Compile, packageSrc) := true

sources in doc in Test := Nil

play.Project.playJavaSettings