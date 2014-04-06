import sbt._

// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository 
resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype Public Repository" at "https://oss.sonatype.org/content/groups/public"
)

lazy val root = project
  .in(file("."))
  .dependsOn(sbtPlessPlugin % "test->test;compile->compile")

lazy val sbtPlessPlugin = uri("git://github.com/urbas/sbt-pless.git#master")
//lazy val sbtPlessPlugin = file("../../sbt-pless")


addSbtPlugin("de.johoop" % "jacoco4sbt" % "2.1.4")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "0.8.3")

addSbtPlugin("com.typesafe.sbt" % "sbt-pgp" % "0.8.3")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "0.2.1")

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.2")
