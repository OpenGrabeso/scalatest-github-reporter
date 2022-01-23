package net.opengrabeso.test

import net.opengrabeso.test.GitHubAnnotationsReporter._
import org.scalatest.ResourcefulReporter
import org.scalatest.events._
import org.scalatest.exceptions.TestFailedException

import java.io.{File, FileOutputStream, PrintWriter}
import java.nio.file.{Files, Path, Paths}
import scala.collection.mutable

object GitHubAnnotationsReporter {
  def escape(s: String) = s.replace("\"", "\\\"")
  def annotationFromLoc(level: String, loc: Option[(String, Int)], title: String, message: String): Option[String] = {
    loc match {
      case Some((file, lineNumber)) =>
        Some(s"""  {"annotation_level":"$level","path":"$file","start_line":$lineNumber,"end_line":$lineNumber,"title":"${escape(title)}","message":"${escape(message)}"}""")
      case None =>
        None
    }
  }

  def loc(location: Option[Location], throwable: Option[Throwable]): Option[(String, Int)] = {
    def name(file: String, path: Option[String]): String = {
      path match {
        // p may contain: "Please set the environment variable SCALACTIC_FILL_FILE_PATHNAMES to yes...
        case Some(p) if p.endsWith(file) =>
          val curDir = Paths.get(new File(".").getCanonicalPath)
          curDir.relativize(Paths.get(p)).toString
        case _ =>
          file
      }
    }
    (location, throwable) match {
      case (Some(inFile: LineInFile), _) =>
        Some((name(inFile.fileName, inFile.filePathname), inFile.lineNumber))
      case (Some(SeeStackDepthException), Some(ex: TestFailedException)) =>
        ex.position.map(pos => (name(pos.fileName, Some(pos.filePathname)), pos.lineNumber))
      case _ =>
        None
    }
  }

  def annotation(level: String, location: Option[Location], throwable: Option[Throwable], title: String, message: String): Option[String] = {
    annotationFromLoc(level, loc(location, throwable), title, message)
  }
}

class GitHubAnnotationsReporter extends ResourcefulReporter {
  Files.createDirectories(Paths.get("target/test-reports"))

  private val stream = new FileOutputStream("target/test-reports/github-annotations.json")
  private val writer = new PrintWriter(stream)

  private val buf = mutable.ArrayBuffer.empty[String]
  // https://docs.github.com/en/rest/reference/checks#annotations-object

  override def dispose(): Unit = {
    writer.print(buf.mkString("[\n", ",\n", "\n]\n"))
    writer.close()
    stream.close()
  }

  override def apply(event: Event): Unit = {
    event match {
      case TestStarting(ordinal, suiteName, suiteId, suiteClassName, testName, testText, formatter, location, rerunner, payload, threadName, timeStamp) =>
      case TestSucceeded(ordinal, suiteName, suiteId, suiteClassName, testName, testText, recordedEvents, duration, formatter, location, rerunner, payload, threadName, timeStamp) =>
      case TestFailed(ordinal, message, suiteName, suiteId, suiteClassName, testName, testText, recordedEvents, analysis, throwable, duration, formatter, location, rerunner, payload, threadName, timeStamp) =>
        buf ++= annotation("failure", location, throwable, suiteName, s"'$testName' failed: $message")
      case TestIgnored(ordinal, suiteName, suiteId, suiteClassName, testName, testText, formatter, location, payload, threadName, timeStamp) =>
      case TestPending(ordinal, suiteName, suiteId, suiteClassName, testName, testText, recordedEvents, duration, formatter, location, payload, threadName, timeStamp) =>
      case TestCanceled(ordinal, message, suiteName, suiteId, suiteClassName, testName, testText, recordedEvents, throwable, duration, formatter, location, rerunner, payload, threadName, timeStamp) =>
        buf ++= annotation("failure", location, throwable, suiteName, s"'$testName' failed: $message")
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
