
ThisBuild / version := "0.3.2"

ThisBuild / scalaVersion := "2.13.8"

ThisBuild / githubOwner := "OpenGrabeso"

ThisBuild / githubRepository := "scalatest-github-reporter"

ThisBuild / githubTokenSource := TokenSource.GitConfig("github.token") || TokenSource.Environment("ORG_TOKEN")

ThisBuild / publishMavenStyle := true

lazy val root = (project in file("."))
  .settings(
    name := "scalatest-github-reporter",
    organization := "net.opengrabeso",

    githubTokenSource := TokenSource.GitConfig("github.token") || TokenSource.Environment("ORG_TOKEN"),

    scalacOptions ++= Seq("-deprecation", "-unchecked"),
    crossScalaVersions := Seq("2.12.15", "2.13.8"),

    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9",

    Test / testOptions += Tests.Argument(
      TestFrameworks.ScalaTest, "-o",
      "-C", "net.opengrabeso.test.GitHubAnnotationsReporter",
      "-C", "net.opengrabeso.test.GitHubActionReporter",
      "-C", "net.opengrabeso.test.MDReporter"
    ),
  )
