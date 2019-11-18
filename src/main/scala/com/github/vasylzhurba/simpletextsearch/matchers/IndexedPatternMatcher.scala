package com.github.vasylzhurba.simpletextsearch.matchers

import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}

trait IndexedPatternMatcher extends PatternMatcher with Ranking with WordSplitter {
  import IndexedPatternMatcher._
  implicit def ec: ExecutionContext
  def content: String

  lazy val index: Future[Index] = Future(
    IndexedPatternMatcher.createIndex(splitWords(content), Map.empty)
  )

  def matchProbability(pattern: String): Future[Double] = index.map { idx =>
    val words      = splitWords(pattern)
    val allMatches = findMatches(words.reverse, idx, List(Nil))
    rank(words, allMatches)
  }
}

object IndexedPatternMatcher {

  @tailrec
  final def createIndex(words: Words, index: Index): Index = words match {
    case first :: next :: rest =>
      createIndex(
        next :: rest,
        index.updatedWith(first)(_.fold(Some(Set(next)))(xs => Some(xs + next)))
      )
    case first :: Nil =>
      createIndex(Nil, index.updatedWith(first)(_.fold(Some(Set.empty[String]))(Some(_))))
    case Nil => index
  }

  @tailrec
  final def findMatches(
      reversedPatterns: Words,
      index: Index,
      allMatches: List[Words]
  ): List[Words] =
    reversedPatterns match {
      case Nil => allMatches.filter(_.nonEmpty)
      case x :: xs =>
        index.get(x) match {
          case Some(followingWords) if allMatches.head.headOption.exists(followingWords) =>
            findMatches(xs, index, (x :: allMatches.head) :: allMatches.tail)
          case Some(_) => findMatches(xs, index, (x :: Nil) :: allMatches)
          case None    => findMatches(xs, index, Nil :: allMatches)
        }
    }
}
