logLevel := Level.Warn

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype Public Repository" at "https://oss.sonatype.org/content/groups/public"
)

addSbtPlugin("si.urbas" % "sbt-pless" % "0.0.10-SNAPSHOT")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "0.8.3")

addSbtPlugin("com.typesafe.sbt" % "sbt-pgp" % "0.8.3")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "0.2.1")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.0")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")