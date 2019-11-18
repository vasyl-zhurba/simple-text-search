package com.github.vasylzhurba.simpletextsearch.matchers

import java.nio.file.Paths

import org.scalatest.{AsyncFlatSpec, LoneElement, Matchers}

class FilePatternMatcherTest extends AsyncFlatSpec with Matchers with LoneElement {
  val matcher = new FilePatternMatcher(Paths.get("src/test/resources/files/1.txt")) with TestRanking
  "File matcher" should "index file's content" in {
    matcher.index.map(
      _ shouldBe Map(
        "one"   -> Set("two"),
        "two"   -> Set("three"),
        "three" -> Set.empty
      )
    )
  }

  it should "search for pattern" in {
    for {
      r1 <- matcher.matchProbability("one two")
      r2 <- matcher.matchProbability("three")
      r3 <- matcher.matchProbability("four")
      r4 <- matcher.matchProbability("")
    } yield {
      r1 shouldBe 2
      r2 shouldBe 1
      r3 shouldBe 0
      r4 shouldBe 0
    }
  }

  import FilePatternMatcher._
  val anotherMatcher = new FilePatternMatcher(Paths.get("src/test/resources/files/3.txt"))
  with TestRanking
  "Top matchers" should "return empty result for empty matchers" in {
    val matcher = topMatches(Nil, 10)
    matcher("one").map(_ shouldBe empty)
  }

  it should "return empty result if nothing found" in {
    val topMatcher = topMatches(matcher :: anotherMatcher :: Nil, 10)
    topMatcher("four").map(_ shouldBe empty)
  }

  it should "order results by rank" in {
    val topMatcher = topMatches(matcher :: anotherMatcher :: Nil, 10)
    topMatcher("two three").map(
      _ should contain theSameElementsInOrderAs Seq(matcher.path -> 2, anotherMatcher.path -> 1)
    )
  }

  it should "order results by file name if rank is the same" in {
    val topMatcher = topMatches(anotherMatcher :: matcher :: Nil, 10)
    topMatcher("one three").map(
      _ should contain theSameElementsInOrderAs Seq(matcher.path -> 1, anotherMatcher.path -> 1)
    )
  }

  it should "return results with rank > 0" in {
    val topMatcher = topMatches(matcher :: anotherMatcher :: Nil, 10)
    topMatcher("two").map(_.loneElement shouldBe matcher.path -> 1)
  }

  it should "return top requested results" in {
    val topMatcher = topMatches(matcher :: anotherMatcher :: Nil, 1)
    topMatcher("two three").map(_.loneElement shouldBe matcher.path -> 2)
  }

}
