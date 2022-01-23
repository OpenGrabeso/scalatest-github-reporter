
ThisBuild / version := "0.1.0"

ThisBuild / scalaVersion := "2.13.8"

ThisBuild / githubOwner := "OpenGrabeso"

ThisBuild / githubRepository := "scalatest-github-reporter"

ThisBuild / githubActor := sys.env.getOrElse("ORG_USERNAME", "OpenGrabeso")

ThisBuild / githubTokenSource := TokenSource.GitConfig("github.token") || TokenSource.Environment("ORG_TOKEN") || TokenSource.Environment("GITHUB_TOKEN")

ThisBuild / publishMavenStyle := true

lazy val root = (project in file("."))
  .settings(
    name := "scalatest-github-reporter",
    organization := "net.opengrabeso",

    scalacOptions ++= Seq("-deprecation", "-unchecked"),
    crossScalaVersions := Seq("2.12.14", "2.13.8"),

    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9",

    Test / testOptions += Tests.Argument(
      TestFrameworks.ScalaTest, "-o",
      "-C", "net.opengrabeso.test.GitHubAnnotationsReporter",
      "-C", "net.opengrabeso.test.GitHubActionReporter",
      "-C", "net.opengrabeso.test.MDReporter"
    ),
  )
