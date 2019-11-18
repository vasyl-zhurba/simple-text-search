package com.github.vasylzhurba.simpletextsearch.matchers

import java.nio.file.{Files, Path}

import scala.concurrent.{ExecutionContext, Future}

class FilePatternMatcher(val path: Path)(implicit val ec: ExecutionContext)
    extends IndexedPatternMatcher
    with FullMatchRanking {
  def content: String = new String(Files.readAllBytes(path))
}

object FilePatternMatcher {
  def topMatches(matchers: List[FilePatternMatcher], itemsToShow: Int)(
      implicit ec: ExecutionContext
  ): String => Future[List[(Path, Double)]] =
    pattern =>
      Future
        .sequence(matchers.map(x => x.matchProbability(pattern).map(x.path -> _)))
        .map { xs =>
          implicit val ord: Ordering[Double] = scala.Ordering.Double.TotalOrdering.reverse
          xs.filter { case (_, rank) => rank > 0 }
            .sortBy(_.swap)
            .take(itemsToShow)

        }
}
