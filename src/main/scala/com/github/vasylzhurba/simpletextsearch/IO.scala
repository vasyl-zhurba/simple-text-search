package com.github.vasylzhurba.simpletextsearch

import java.nio.file.{InvalidPathException, NoSuchFileException, NotDirectoryException, Path, Paths}
import java.text.NumberFormat

import scala.annotation.tailrec
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future, TimeoutException}
import scala.jdk.CollectionConverters._
import scala.util.{Failure, Success, Try}

object IO {
  val percentFormat: NumberFormat = NumberFormat.getPercentInstance

  @tailrec
  final def iterate(
      matcher: String => Future[List[(Path, Double)]],
      searchTimeout: Duration
  )(implicit ec: ExecutionContext): Either[IOError, Unit] = {
    print("search> ")
    scala.io.StdIn.readLine match {
      case ":quit" => Right(println("Good bye!"))
      case searchString =>
        Try {
          Await.result(matcher(searchString), searchTimeout) match {
            case Nil => println("no matches found")
            case xs =>
              xs.foreach { case (path, rank) => println(s"$path - ${percentFormat.format(rank)}") }
          }
        } match {
          case Success(_)                   => iterate(matcher, searchTimeout)
          case Failure(_: TimeoutException) => Left(SearchTimeout(searchTimeout))
          case Failure(ex)                  => Left(SystemError(ex))
        }
    }
  }

  def textFiles(directory: Option[String]): Either[IOError, List[Path]] = {
    directory
      .toRight[IOError](MissingPathArg)
      .flatMap { path =>
        Try {
          val files = java.nio.file.Files
            .newDirectoryStream(Paths.get(path), "*.txt")
            .asScala
            .toList
          println(s"${files.size} files read in directory $path")
          files
        }.toEither.left
          .map {
            case _: InvalidPathException  => InvalidPath(path)
            case _: NoSuchFileException   => InvalidPath(path)
            case _: NotDirectoryException => NotDirectory(path)
            case ex                       => SystemError(ex)
          }
      }
  }

  val printError: IOError => Unit = (error: IOError) => System.err.println(error)

  sealed trait IOError                         extends Product with Serializable
  case object MissingPathArg                   extends IOError
  case class InvalidPath(path: String)         extends IOError
  case class NotDirectory(path: String)        extends IOError
  case class SearchTimeout(duration: Duration) extends IOError
  case class SystemError(error: Throwable)     extends IOError
}
