package com.github.vasylzhurba.simpletextsearch.matchers

trait Ranking {
  def rank(searched: Words, found: Seq[Words]): Double
}

trait FullMatchRanking extends Ranking {
  def rank(searched: Words, found: Seq[Words]): Double = if (found.contains(searched)) 1 else 0
}

trait WordMatchRanking extends Ranking {
  abstract override def rank(searched: Words, found: Seq[Words]): Double =
    super.rank(searched, found) match {
      case 1d => 1
      case _ =>
        implicit val wordsOrdering: Ordering[Words] =
          Ordering
            .by[Words, (Int, String)](words => (words.size, words.mkString))
            .reverse
        found.sorted.headOption.fold(0d)(_.size / searched.size.toDouble)
    }
}
