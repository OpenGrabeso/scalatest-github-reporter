package net.opengrabeso.test

import net.opengrabeso.test.GitHubActionReporter._
import net.opengrabeso.test.GitHubAnnotationsReporter._
import org.scalatest.Reporter
import org.scalatest.events._

object GitHubActionReporter {
  def locationString(location: Option[Location], throwable: Option[Throwable]): String = {
    loc(location, throwable) match {
      case Some((file, lineNumber)) =>
        s"file=$file,line=$lineNumber,"
      case _ =>
        ""
    }
  }
}

class GitHubActionReporter extends Reporter {

  override def apply(event: Event): Unit = {
    event match {
      case TestStarting(ordinal, suiteName, suiteId, suiteClassName, testName, testText, formatter, location, rerunner, payload, threadName, timeStamp) =>
      case TestSucceeded(ordinal, suiteName, suiteId, suiteClassName, testName, testText, recordedEvents, duration, formatter, location, rerunner, payload, threadName, timeStamp) =>
      case TestFailed(ordinal, message, suiteName, suiteId, suiteClassName, testName, testText, recordedEvents, analysis, throwable, duration, formatter, location, rerunner, payload, threadName, timeStamp) =>
        println(s"::error ${locationString(location, throwable)}title=$suiteName::'$testName' failed: $message")
      case TestIgnored(ordinal, suiteName, suiteId, suiteClassName, testName, testText, formatter, location, payload, threadName, timeStamp) =>
      case TestPending(ordinal, suiteName, suiteId, suiteClassName, testName, testText, recordedEvents, duration, formatter, location, payload, threadName, timeStamp) =>
      case TestCanceled(ordinal, message, suiteName, suiteId, suiteClassName, testName, testText, recordedEvents, throwable, duration, formatter, location, rerunner, payload, threadName, timeStamp) =>
        println(s"::error ${locationString(location, throwable)}title=$suiteName::'$testName' canceled: $message")
      case SuiteStarting(ordinal, suiteName, suiteId, suiteClassName, formatter, location, rerunner, payload, threadName, timeStamp) =>
      case SuiteCompleted(ordinal, suiteName, suiteId, suiteClassName, duration, formatter, location, rerunner, payload, threadName, timeStamp) =>
      case SuiteAborted(ordinal, message, suiteName, suiteId, suiteClassName, throwable, duration, formatter, location, rerunner, payload, threadName, timeStamp) =>
      case RunStarting(ordinal, testCount, configMap, formatter, location, payload, threadName, timeStamp) =>
      case RunCompleted(ordinal, duration, summary, formatter, location, payload, threadName, timeStamp) =>
      case RunStopped(ordinal, duration, summary, formatter, location, payload, threadName, timeStamp) =>
      case RunAborted(ordinal, message, throwable, duration, summary, formatter, location, payload, threadName, timeStamp) =>
      case InfoProvided(ordinal, message, nameInfo, throwable, formatter, location, payload, threadName, timeStamp) =>
        println(s"::notice ${locationString(location, throwable)}:: $message")
      case AlertProvided(ordinal, message, nameInfo, throwable, formatter, location, payload, threadName, timeStamp) =>
        println(s"::warning ${locationString(location, throwable)}:: $message")
      case NoteProvided(ordinal, message, nameInfo, throwable, formatter, location, payload, threadName, timeStamp) =>
        println(s"::notice ${locationString(location, throwable)}:: $message")
      case MarkupProvided(ordinal, text, nameInfo, formatter, location, payload, threadName, timeStamp) =>
      case ScopeOpened(ordinal, message, nameInfo, formatter, location, payload, threadName, timeStamp) =>
      case ScopeClosed(ordinal, message, nameInfo, formatter, location, payload, threadName, timeStamp) =>
      case ScopePending(ordinal, message, nameInfo, formatter, location, payload, threadName, timeStamp) =>
      case DiscoveryStarting(ordinal, configMap, threadName, timeStamp) =>
      case DiscoveryCompleted(ordinal, duration, threadName, timeStamp) =>
    }
  }
}
