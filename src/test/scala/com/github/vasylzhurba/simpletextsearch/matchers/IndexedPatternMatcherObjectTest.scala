package com.github.vasylzhurba.simpletextsearch.matchers

import org.scalatest.{FlatSpec, LoneElement, Matchers}

class IndexedPatternMatcherObjectTest extends FlatSpec with Matchers with LoneElement {
  import IndexedPatternMatcher._
  "Create index" should "return empty index for empty words" in {
    createIndex(Nil, Map.empty) shouldBe empty
  }

  it should "return single value map without following words for single word" in {
    createIndex("word" :: Nil, Map.empty) shouldBe Map("word" -> Set.empty[String])
  }

  it should "build map with correct following words" in {
    createIndex("one" :: "two" :: "three" :: "four" :: Nil, Map.empty) shouldBe
      Map(
        "one"   -> Set("two"),
        "two"   -> Set("three"),
        "three" -> Set("four"),
        "four"  -> Set.empty
      )

    createIndex("to" :: "be" :: "or" :: "not" :: "to" :: "be" :: Nil, Map.empty) shouldBe
      Map(
        "to"  -> Set("be"),
        "be"  -> Set("or"),
        "or"  -> Set("not"),
        "not" -> Set("to")
      )
  }

  it should "return map with multiple following words" in {
    createIndex(
      "one" :: "two" :: "one" :: "three" :: "one" :: "four" :: "five" :: "four" :: "six" :: Nil,
      Map.empty
    ) shouldBe
      Map(
        "one"   -> Set("two", "three", "four"),
        "two"   -> Set("one"),
        "three" -> Set("one"),
        "four"  -> Set("five", "six"),
        "five"  -> Set("four"),
        "six"   -> Set.empty
      )
  }

  it should "return map duplicate following words" in {
    createIndex("one" :: "one" :: "one" :: "one" :: "two" :: "one" :: Nil, Map.empty) shouldBe
      Map(
        "one" -> Set("one", "two"),
        "two" -> Set("one")
      )
  }

  "Find matches" should "return empty result for empty pattern" in {
    findMatches(Nil, createIndex("one" :: "two" :: "three" :: "four" :: Nil, Map.empty), List(Nil)) shouldBe empty
  }

  it should "return empty result for no match" in {
    findMatches(
      "five" :: Nil,
      createIndex("one" :: "two" :: "three" :: "four" :: Nil, Map.empty),
      List(Nil)
    ) shouldBe empty
  }

  it should "return single matches" in {
    findMatches(
      "one" :: "two" :: "three" :: "four" :: Nil,
      createIndex("one" :: "two" :: "three" :: "four" :: Nil, Map.empty),
      List(Nil)
    ) should contain theSameElementsAs List("one") :: List("two") :: List("three") :: List("four") :: Nil
  }

  it should "return full match" in {
    findMatches(
      "four" :: "three" :: "two" :: "one" :: Nil,
      createIndex("one" :: "two" :: "three" :: "four" :: Nil, Map.empty),
      List(Nil)
    ).loneElement should contain theSameElementsInOrderAs "one" :: "two" :: "three" :: "four" :: Nil
  }

  it should "return partial matches" in {
    findMatches(
      "five" :: "four" :: "two" :: "one" :: Nil,
      createIndex("one" :: "two" :: "three" :: "four" :: "five" :: Nil, Map.empty),
      List(Nil)
    ) should contain theSameElementsAs ("one" :: "two" :: Nil) :: ("four" :: "five" :: Nil) :: Nil
  }
}
