package com.github.vasylzhurba.simpletextsearch

import java.nio.file.Path

import com.github.vasylzhurba.simpletextsearch.matchers._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Main extends App {
  val fileMatcher: Path => FilePatternMatcher =
    new FilePatternMatcher(_) with UpperCaseWordSplitter with IgnorePunctuationMarksWordSplitter
    with WordMatchRanking
  val searchTimeout            = 1.minute
  val numberOfTopMatchesToShow = 10
  val matchingFunc             = FilePatternMatcher.topMatches(_, numberOfTopMatchesToShow)

  val program = for {
    paths    <- IO.textFiles(args.headOption)
    matchers = paths.map(fileMatcher)
    _        = matchers.foreach(_.index) //start indexing in background
    _        <- IO.iterate(matchingFunc(matchers), searchTimeout)
  } yield ()

  program.fold(IO.printError, identity)
}
