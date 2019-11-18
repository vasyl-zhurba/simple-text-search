package com.github.vasylzhurba.simpletextsearch.matchers

trait WordSplitter {
  def separatorRegex                  = "\\s+"
  def splitWords(text: String): Words = text.split(separatorRegex).toList.filter(_.nonEmpty)
}

trait UpperCaseWordSplitter extends WordSplitter {
  abstract override def splitWords(text: String): Words = super.splitWords(text).map(_.toUpperCase)
}

trait IgnorePunctuationMarksWordSplitter extends WordSplitter {
  abstract override def separatorRegex: String = "[\\p{Punct}\\s]+"
}
