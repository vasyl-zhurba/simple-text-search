package com.github.vasylzhurba.simpletextsearch

import java.nio.file.Paths

import org.scalatest.{EitherValues, FlatSpec, LoneElement, Matchers}

class IOTest extends FlatSpec with EitherValues with Matchers {

  import IO._
  "Text files" should "return error for empty argument" in {
    textFiles(None).left.value shouldBe MissingPathArg
  }

  it should "return error for invalid path" in {
    textFiles(Some("\u0000")).left.value shouldBe a[InvalidPath]
    textFiles(Some("wrong url")).left.value shouldBe a[InvalidPath]
  }

  it should "return error for not a directory" in {
    textFiles(Some(".scalafmt.conf")).left.value shouldBe a[NotDirectory]
  }

  it should "return text files only" in {
    textFiles(Some("src/test/resources/files"))
      .getOrElse(fail) should contain theSameElementsAs Seq(
      Paths.get("src/test/resources/files/1.txt"),
      Paths.get("src/test/resources/files/3.txt")
    )
  }
}
