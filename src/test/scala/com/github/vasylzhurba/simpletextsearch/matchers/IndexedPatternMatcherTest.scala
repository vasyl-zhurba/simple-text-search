package com.github.vasylzhurba.simpletextsearch.matchers

import org.scalatest.{AsyncFlatSpec, Matchers}

import scala.concurrent.ExecutionContext

class IndexedPatternMatcherTest extends AsyncFlatSpec with Matchers {
  class TextMatcher(val content: String)(implicit val ec: ExecutionContext)
      extends IndexedPatternMatcher
      with FullMatchRanking
      with TestRanking

  "Matcher" should "match with probability" in {
    val m = new TextMatcher("one two three four five")
    for {
      r1 <- m.matchProbability("one two three four five")
      r2 <- m.matchProbability("two three four five")
      r3 <- m.matchProbability("one two three four")
      r4 <- m.matchProbability("three")
      r5 <- m.matchProbability("six seven")
      r6 <- m.matchProbability("")
      r7 <- m.matchProbability("five four three two one")
    } yield {
      r1 shouldBe 5
      r2 shouldBe 4
      r3 shouldBe 4
      r4 shouldBe 1
      r5 shouldBe 0
      r6 shouldBe 0
      r7 shouldBe 1
    }
  }
}
