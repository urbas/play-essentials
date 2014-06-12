import si.urbas.sbtutils.docs._

scalaVersion := "2.11.1"

lazy val root = Projects.root(project.in(file(".")), pless, plessTest, plessJpa, plessJpaTest, plessJpaSample)

lazy val pless = Projects.pless(project)

lazy val plessTest = Projects.plessTest(project, pless)

lazy val plessJpa = Projects.plessJpa(project, pless)

lazy val plessJpaTest = Projects.plessJpaTest(project, pless, plessJpa, plessTest)

lazy val plessJpaSample = Projects.plessJpaSample(project, pless, plessJpa, plessTest, plessJpaTest)

docsSnippetDirs ++= Seq("app/si/urbas/pless", "test/si/urbas/pless", "conf").map(plessJpaSample.base / _)