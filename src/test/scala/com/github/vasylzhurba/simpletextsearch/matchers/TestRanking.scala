package com.github.vasylzhurba.simpletextsearch.matchers

trait TestRanking extends Ranking {
  abstract override def rank(searched: Words, found: Seq[Words]): Double =
    found.map(_.size).maxOption.getOrElse(0).toDouble
}
