package net.opengrabeso.test

import org.scalatest.ResourcefulReporter
import org.scalatest.events._

import java.io.{FileOutputStream, PrintWriter}
import java.nio.file.{Files, Path, Paths}

class MDReporter extends ResourcefulReporter {
  Files.createDirectories(Paths.get("target/test-reports"))

  private val stream = new FileOutputStream("target/test-reports/test-report.md")
  private val writer = new PrintWriter(stream)


  override def dispose(): Unit = {
    writer.close()
    stream.close()
  }

  override def apply(event: Event): Unit = {
    event match {
      case TestStarting(ordinal, suiteName, suiteId, suiteClassName, testName, testText, formatter, location, rerunner, payload, threadName, timeStamp) =>
      case TestSucceeded(ordinal, suiteName, suiteId, suiteClassName, testName, testText, recordedEvents, duration, formatter, location, rerunner, payload, threadName, timeStamp) =>
      case TestFailed(ordinal, message, suiteName, suiteId, suiteClassName, testName, testText, recordedEvents, analysis, throwable, duration, formatter, location, rerunner, payload, threadName, timeStamp) =>
        writer.println(s"$suiteName / **$testName** failed: $message")
        writer.println()
      case TestIgnored(ordinal, suiteName, suiteId, suiteClassName, testName, testText, formatter, location, payload, threadName, timeStamp) =>
      case TestPending(ordinal, suiteName, suiteId, suiteClassName, testName, testText, recordedEvents, duration, formatter, location, payload, threadName, timeStamp) =>
      case TestCanceled(ordinal, message, suiteName, suiteId, suiteClassName, testName, testText, recordedEvents, throwable, duration, formatter, location, rerunner, payload, threadName, timeStamp) =>
        writer.println(s"$suiteName / **$testName** canceled: $message")
        writer.println()
      case SuiteStarting(ordinal, suiteName, suiteId, suiteClassName, formatter, location, rerunner, payload, threadName, timeStamp) =>
      case SuiteCompleted(ordinal, suiteName, suiteId, suiteClassName, duration, formatter, location, rerunner, payload, threadName, timeStamp) =>
      case SuiteAborted(ordinal, message, suiteName, suiteId, suiteClassName, throwable, duration, formatter, location, rerunner, payload, threadName, timeStamp) =>
      case RunStarting(ordinal, testCount, configMap, formatter, location, payload, threadName, timeStamp) =>
      case RunCompleted(ordinal, duration, summary, formatter, location, payload, threadName, timeStamp) =>
      case RunStopped(ordinal, duration, summary, formatter, location, payload, threadName, timeStamp) =>
      case RunAborted(ordinal, message, throwable, duration, summary, formatter, location, payload, threadName, timeStamp) =>
      case InfoProvided(ordinal, message, nameInfo, throwable, formatter, location, payload, threadName, timeStamp) =>
      case AlertProvided(ordinal, message, nameInfo, throwable, formatter, location, payload, threadName, timeStamp) =>
      case NoteProvided(ordinal, message, nameInfo, throwable, formatter, location, payload, threadName, timeStamp) =>
      case MarkupProvided(ordinal, text, nameInfo, formatter, location, payload, threadName, timeStamp) =>
      case ScopeOpened(ordinal, message, nameInfo, formatter, location, payload, threadName, timeStamp) =>
      case ScopeClosed(ordinal, message, nameInfo, formatter, location, payload, threadName, timeStamp) =>
      case ScopePending(ordinal, message, nameInfo, formatter, location, payload, threadName, timeStamp) =>
      case DiscoveryStarting(ordinal, configMap, threadName, timeStamp) =>
      case DiscoveryCompleted(ordinal, duration, threadName, timeStamp) =>
    }
  }
}
