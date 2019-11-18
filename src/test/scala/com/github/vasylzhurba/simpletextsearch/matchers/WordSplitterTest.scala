package com.github.vasylzhurba.simpletextsearch.matchers

import org.scalatest.{FlatSpec, Matchers}

class WordSplitterTest extends FlatSpec with Matchers {
  class SimpleSplitter extends WordSplitter
  val simpleSplitter: WordSplitter = new SimpleSplitter
  "Simple splitter" should "split words by whitespaces" in {
    simpleSplitter.splitWords("one two\tthree\nfour") shouldBe List("one", "two", "three", "four")
  }

  it should "return empty list for empty text" in {
    simpleSplitter.splitWords("") shouldBe empty
  }

  it should "return empty list for spaces only text" in {
    simpleSplitter.splitWords(" \t\n\r") shouldBe empty
  }

  val upperCaseSplitter: WordSplitter = new SimpleSplitter with UpperCaseWordSplitter
  "Upper case splitter" should "split words by whitespaces and make them uppercase" in {
    upperCaseSplitter.splitWords("one two\toNe\nfOUr") shouldBe List(
      "ONE",
      "TWO",
      "ONE",
      "FOUR"
    )
  }

  val ignorePunctuationMarksSplitter: WordSplitter = new SimpleSplitter
  with IgnorePunctuationMarksWordSplitter

  "Ignore punctuation mark splitter" should "split words by whitespaces and punctuation marks" in {
    ignorePunctuationMarksSplitter.splitWords("one,two\tthree\nfour.five!six,\"seven\"") shouldBe List(
      "one",
      "two",
      "three",
      "four",
      "five",
      "six",
      "seven"
    )
  }
  val combinedSplitter: WordSplitter = new SimpleSplitter with IgnorePunctuationMarksWordSplitter
  with UpperCaseWordSplitter

  "Combined splitter" should "split words by whitespaces and punctuation marks" in {
    combinedSplitter.splitWords("one,tWo\ttHRee\nfoUR.five!six,\"seven\"") shouldBe List(
      "ONE",
      "TWO",
      "THREE",
      "FOUR",
      "FIVE",
      "SIX",
      "SEVEN"
    )
  }
}
