package com.exercise.two

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import com.exercise.two.Main._

class MainTest extends AnyWordSpec {

  val room1 = RoomPrice(1, 100.0)
  val room2 = RoomPrice(2, 200.0)

  val beer1 = RoomServiceCharges(4,"beer", 3.75, "01/10/2020")
  val beer2 = RoomServiceCharges(4,"beer", 3.75, "02/10/2020")
  val beer3 = RoomServiceCharges(4,"beer", 3.75, "03/10/2020")
  val vodka1 = RoomServiceCharges(4,"bottle of vodka", 20.10, "04/10/2020")
  val vodka2 = RoomServiceCharges(4,"bottle of vodka", 20.10, "08/10/2020")

  val booking1 = Booking(1,1,1, "12",1, "10/10/2020", 1 )
  val booking2 = Booking(2,1,2, "13",1, "10/10/2020", 1 )
  val booking3 = Booking(3,1,3, "22",2, "10/10/2020", 1)
  val booking4 = Booking(4,2,4, "PentHouse",2, "01/10/2020", 8 )
  val booking5 = Booking(5,2,4, "1",1, "20/10/2020", 1 )

  val account1 = Account(1,"BusyPeopleInc",0)
  val account2 = Account(2,"Mr Blank Name",50)

  val person1 = Person(1,"Person","One")
  val person2 = Person(2,"Person","Two")
  val person3 = Person(3,"Person","Three")
  val person4 = Person(4,"Blank","Name")

  val accountList: List[Account] = List(account1, account2)
  val bookingList:List[Booking] = List(booking1, booking2, booking3, booking4, booking5)
  val roomPriceList: List[RoomPrice] = List(room1, room2)
  val roomServiceList: List[RoomServiceCharges] = List(beer1, beer2, beer3, vodka1, vodka2)

  "Exercise 2" should {

    "applyDiscount if no discount dont change totalRoomCost" in {
      val actual = applyDiscount(0, 100)
      actual shouldBe 100
    }

    "applyDiscount if discount 50% return 50" in {
      val actual = applyDiscount(50, 100)
      actual shouldBe 50
    }

    "filterBookingsByAccountId returns list of bookings for given account id" in {
      val actual = filterBookingsByAccountId(bookingList, 1)
      val expect = List(booking1, booking2, booking3)
      actual shouldBe expect
    }

    "filterBookingsByAccountId returns empty list for non-existent account id" in {
      val actual = filterBookingsByAccountId(bookingList, 3)
      val expect = List()
      actual shouldBe expect
    }

    "lookupRoomPrice returns correct price for given room type id" in {
      val actual = lookupRoomPrice(roomPriceList, 1)
      actual shouldBe 100
    }

    "calculateRoomCost returns 0 given empty list" in {
      val testBookingList = List()
      val actual = calculateRoomCost(testBookingList, roomPriceList)
      actual shouldBe 0
    }

    "calculateRoomCost returns correct room cost for given booking" in {
      val testBookingList = List(booking1)
      val actual = calculateRoomCost(testBookingList, roomPriceList)
      actual shouldBe 100
    }

    "calculateRoomCost returns correct total room cost given list of bookings" in {
      val testBookingList = List(booking1, booking2, booking3)
      val actual = calculateRoomCost(testBookingList, roomPriceList)
      actual shouldBe 400
    }

    "filterChargesByBookingId returns empty list for given booking id with no room service charges" in {
      val actual = filterChargesByBookingId(roomServiceList, 1)
      val expect = List()
      actual shouldBe expect
    }

    "filterChargesByBookingId returns list of charges for given booking id with room service charges" in {
      val actual = filterChargesByBookingId(roomServiceList, 4)
      val expect = roomServiceList
      actual shouldBe expect
    }

    "filterChargesByBookingId filters room service list and returns relevant charges only" in {
      val testBeer = RoomServiceCharges(6,"beer", 3.75, "01/10/2020")
      val testRoomServiceList = roomServiceList :+ testBeer
      val actual = filterChargesByBookingId(testRoomServiceList, 6)
      val expect = List(testBeer)
      actual shouldBe expect
    }

    "collateAccountCharges returns empty list given empty list" in {
      val testBookingList = List()
      val actual = collateAccountCharges(testBookingList, roomServiceList)
      val expect = List()
      actual shouldBe expect
    }

    "collateAccountCharges returns empty list given list of bookings with no room service charges" in {
      val testBookingList = List(booking1, booking2, booking3)
      val actual = collateAccountCharges(testBookingList, roomServiceList)
      val expect = List()
      actual shouldBe expect
    }

    "collateAccountCharges returns list of charges given list of bookings where one has room service charges" in {
      val testBookingList = List(booking4, booking5)
      val actual = collateAccountCharges(testBookingList, roomServiceList)
      val expect = roomServiceList
      actual shouldBe expect
    }

    "collateAccountCharges collates list of charges given list of bookings where more than one has room service charges" in {
      val testBooking = Booking(6,1,1, "12",1, "10/10/2020", 1 )
      val testVodka = RoomServiceCharges(6,"bottle of vodka", 20.10, "04/10/2020")
      val testBeer = RoomServiceCharges(7,"beer", 3.75, "01/10/2020")
      val testBookingList = List(booking4, booking5, testBooking)
      val testRoomServiceList = roomServiceList ++ List(testVodka, testBeer)
      val actual = collateAccountCharges(testBookingList, testRoomServiceList)
      val expect = roomServiceList :+ testVodka
      actual shouldBe expect
    }

    "calculateRoomServiceTotal returns 0 given empty list" in {
      val testRoomServiceList = List()
      val actual = calculateRoomServiceTotal(testRoomServiceList)
      actual shouldBe 0
    }

    "calculateRoomServiceTotal returns correct total given list of room service charges" in {
      val actual = calculateRoomServiceTotal(roomServiceList)
      actual shouldBe 51.45
    }

    "produceInvoice returns AccountInvoice object" in {
      val actual = produceInvoice(account1, bookingList, roomPriceList, roomServiceList)
      actual shouldBe a [AccountInvoice]
    }

    "produceInvoice returns AccountInvoice with correct total for account1" in {
      val invoice1 = produceInvoice(account1, bookingList, roomPriceList, roomServiceList)
      val actual = invoice1.total
      actual shouldBe 400
    }

    "produceInvoice returns AccountInvoice with correct total for account2" in {
      val invoice2 = produceInvoice(account2, bookingList, roomPriceList, roomServiceList)
      val actual = invoice2.total
      actual shouldBe 901.45
    }
  }
}
