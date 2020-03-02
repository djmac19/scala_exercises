package com.exercise.two

import org.scalatest.funspec.AnyFunSpec
import com.exercise.two.Main._

class MainTest extends AnyFunSpec {
  describe("Exercise 2") {
    val account1 = Account(1,"BusyPeopleInc",0)
    val invoice1 = AccountInvoice(account1)
    it("should return correct room cost") {
//      val expectedResult: Double = 500.0
//      val actualResult = invoice1.roomCost
//      actualResult == expectedResult

    }
  }
}
