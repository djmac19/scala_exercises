package com.exercise.two

class Main extends App {

  case class RoomPrice(roomTypeId: Int, cost: Double)
  case class Person(personId: Int, firstName:String, surname:String )
  case class RoomServiceCharges(bookingId: Int, itemName: String, cost:Double, date:String)
  case class Booking(bookingId: Int, accountId: Int, personId: Int, room: String, roomTypeId: Int, startDate: String, daysBooked: Int)
  case class Account(accountId: Int, accountName: String, discount:Int)

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
  val booking5 = Booking(5,1,4, "1",1, "20/10/2020", 1 )

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

  //please create an output of this per account
  //  case class accountInvoice(account: Account, roomCost: Double, RoomServiceTotal: Double, total: Double)
  case class AccountInvoice(account: Account) {

    val accountBookings: List[Booking] = bookingList.filter(_.accountId == account.accountId)

    def lookupRoomPrice(id: Int): Double = roomPriceList.filter(_.roomTypeId == id).head.cost

    val roomCost: Double = accountBookings.foldLeft(0.0)((total, booking) => {
      total + (lookupRoomPrice(booking.roomTypeId) * booking.daysBooked)
    })

    val accountRoomService: List[RoomServiceCharges] = accountBookings.flatMap(booking => roomServiceList.filter(_.bookingId == booking.bookingId))

    val roomServiceTotal: Double = accountRoomService.foldLeft(0.0)((total, charge) => {
      total + charge.cost
    })

    def applyDiscount(cost: Double): Double = {
      (1 - (account.discount.toDouble / 100)) * cost
    }

    val total: Double = applyDiscount(roomCost) + roomServiceTotal
  }

//  val invoice1 = AccountInvoice(account1)
//  val invoice2 = AccountInvoice(account2)

//  invoice1.accountBookings
//  invoice1.roomCost
//  invoice1.accountRoomService
//  invoice1.roomServiceTotal
//  invoice1.total
//
//  invoice2.accountBookings
//  invoice2.roomCost
//  invoice2.accountRoomService
//  invoice2.roomServiceTotal
//  invoice2.total

}
