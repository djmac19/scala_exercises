
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

  //please create an output of this per account
  case class AccountInvoice(account: Account, roomCost: Double, roomServiceTotal: Double, total: Double)

  def filterBookingsByAccountId(bookingList: List[Booking], accountId: Int): List[Booking] = bookingList.filter(_.accountId == accountId)

  def lookupRoomPrice(roomPriceList: List[RoomPrice], roomTypeId: Int): Double = roomPriceList.filter(_.roomTypeId == roomTypeId).head.cost

  def applyDiscount(discount: Int, cost: Double): Double = {
    (1 - (discount.toDouble / 100)) * cost
  }

  def calculateRoomCost(bookings: List[Booking], roomPriceList: List[RoomPrice]): Double = bookings.foldLeft(0.0)((total, booking) => {
    total + (lookupRoomPrice(roomPriceList, booking.roomTypeId) * booking.daysBooked)
  })

  def filterChargesByBookingId(roomServiceList: List[RoomServiceCharges], bookingId: Int): List[RoomServiceCharges] = roomServiceList.filter(_.bookingId == bookingId)

  def collateAccountCharges(bookings: List[Booking], roomServiceList: List[RoomServiceCharges]): List[RoomServiceCharges] = bookings.flatMap(booking => filterChargesByBookingId(roomServiceList, booking.bookingId) )

  def calculateRoomServiceTotal(charges: List[RoomServiceCharges]): Double = charges.foldLeft(0.0)((total, charge) => {
    total + charge.cost
  })

  def readCSV[A](path: String, listType: String): Unit = {
    val bufferedSource = io.Source.fromFile(path)
    for (line <- bufferedSource.getLines.drop(1)) {
      val cols = line.split("\t").map(_.trim)
      println(cols)
//      listType match {
//        case "bookings" => Booking(cols(0), cols(1), cols(2), cols(3), cols(4), cols(5), cols(6))
//        case "rooms" => RoomPrice(cols(0), cols(1))
//        case "charges" => RoomServiceCharges(cols(0), cols(1), cols(2), cols(3))
//        case "accounts" => Account(cols(0), cols(1), cols(2))
//      }
    }
    bufferedSource.close()
  }

  def produceInvoice(account: Account, bookingList: List[Booking], roomPriceList: List[RoomPrice], roomServiceList: List[RoomServiceCharges]): AccountInvoice = {
    val accountBookings: List[Booking] = filterBookingsByAccountId(bookingList, account.accountId)
    val roomCost: Double = calculateRoomCost(accountBookings, roomPriceList)
    val discountedRoomCost: Double = applyDiscount(account.discount, roomCost)
    val accountRoomServiceCharges: List[RoomServiceCharges] = collateAccountCharges(accountBookings, roomServiceList)
    val roomServiceTotal: Double = calculateRoomServiceTotal(accountRoomServiceCharges)
    val total = discountedRoomCost + roomServiceTotal
    AccountInvoice(account, discountedRoomCost, roomServiceTotal, total)
  }

//  produceInvoice(account1, bookingList, roomPriceList, roomServiceList)
//  produceInvoice(account2, bookingList, roomPriceList, roomServiceList)