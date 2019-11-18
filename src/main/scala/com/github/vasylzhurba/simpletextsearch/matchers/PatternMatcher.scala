package com.github.vasylzhurba.simpletextsearch.matchers

import scala.concurrent.Future

/**
  * Extend this trait for ranked pattern match
  */
trait PatternMatcher {

  /**
    * Matches pattern and returns match probability
    * @param pattern string to be matched
    * @return match probability. Value between 0 and 1 inclusive. 1 means full match, 0 - no match found
    */
  def matchProbability(pattern: String): Future[Double]
}
