# Scalatest Github Reporter

Reporter used to make test result reporting easier for GitHub actions

The project sbt shows how reporters can be used with ScalaTest:

```sbt
    Test / testOptions += Tests.Argument(
      TestFrameworks.ScalaTest, "-o",
      "-C", "net.opengrabeso.test.GitHubAnnotationsReporter",
      "-C", "net.opengrabeso.test.GitHubActionReporter",
      "-C", "net.opengrabeso.test.MDReporter"
    )
```

- Results of GitHubAnnotationsReporter are in target/test-results/annotations.json
- Results of GitHubActionReporter are sent directly to the workflow using [Workflow commands][1]  like `::error`
- Results of MDReporter are in target/test-results/test-report.md

Note: the results are always placed in the `target` folder of the root project, even in a multi-project
build. This is because of how sbt and ScalaTest handle working directory when launching tests.

[1]: https://docs.github.com/en/actions/using-workflows/workflow-commands-for-github-actions#setting-an-error-message
