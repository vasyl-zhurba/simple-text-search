package com.github.vasylzhurba.simpletextsearch.matchers

import org.scalatest.{FlatSpec, Matchers}

class RankingTest extends FlatSpec with Matchers {
  class FullMatchRank extends FullMatchRanking {}
  val fullMatch: Ranking = new FullMatchRank
  "Full match rank" should "return 0 for empty found" in {
    fullMatch.rank("one" :: Nil, Nil) shouldBe 0
  }

  it should "return 1 for full match" in {
    fullMatch.rank(
      "one" :: "two" :: "three" :: Nil,
      Seq(
        "one" :: "two" :: "three" :: Nil,
        "one" :: "two" :: Nil,
        "one" :: Nil
      )
    ) shouldBe 1
  }

  it should "return 0 for partial match" in {
    fullMatch.rank("one" :: "two" :: Nil, Seq("one" :: Nil, "two" :: Nil)) shouldBe 0
    fullMatch.rank("one" :: "three" :: Nil, Seq("one" :: Nil, "three" :: Nil)) shouldBe 0
    fullMatch.rank("one" :: "two" :: "three" :: Nil, Seq("one" :: Nil, "three" :: Nil)) shouldBe 0
    fullMatch.rank("one" :: "two" :: "three" :: Nil, Seq("one" :: "two" :: Nil, "three" :: Nil)) shouldBe 0
  }

  val wordCountRank = new FullMatchRank with WordMatchRanking
  "Word count match rank" should "return 0 for empty found" in {
    wordCountRank.rank("one" :: Nil, Nil) shouldBe 0
  }

  it should "return 1 for full match" in {
    wordCountRank.rank(
      "one" :: "two" :: "three" :: Nil,
      Seq(
        "one" :: "two" :: "three" :: Nil,
        "one" :: "two" :: Nil,
        "one" :: Nil
      )
    ) shouldBe 1
  }

  it should "return rank" in {
    wordCountRank.rank("one" :: "two" :: Nil, Seq("one" :: Nil, "two" :: Nil)) shouldBe 0.5
    wordCountRank.rank("one" :: "three" :: Nil, Seq("one" :: Nil, "three" :: Nil)) shouldBe 0.5
    wordCountRank.rank("one" :: "two" :: "three" :: Nil, Seq("one" :: Nil, "three" :: Nil)) shouldBe 1 / 3d
    wordCountRank.rank("one" :: "two" :: "three" :: Nil, Seq("one" :: "two" :: Nil, "three" :: Nil)) shouldBe 2 / 3d
  }
}
