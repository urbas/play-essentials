// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository 
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("de.johoop" % "jacoco4sbt" % "2.1.4")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "0.8.3")

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.2")
