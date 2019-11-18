package com.github.vasylzhurba.simpletextsearch

package object matchers {
  type Word           = String
  type Words          = List[Word]
  type Index          = Map[Word, FollowingWords]
  type FollowingWords = Set[Word]
}
