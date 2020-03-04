package dave_exercises

object Main extends App {

  case class RoomPrice(roomTypeId: Int, cost: Double)
  case class Person(personId: Int, firstName:String, surname:String )
  case class RoomServiceCharges(bookingId: Int, itemName: String, cost:Double, date:String)
  case class Booking(bookingId: Int, accountId: Int, personId: Int, room: String, roomTypeId: Int, startDate: String, daysBooked: Int)
  case class Account(accountId: Int, accountName: String, discount:Int)

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

//  def readCSV[A](path: String, listType: String): List[A] = {
//    val bufferedSource = io.Source.fromFile(path)
//    for (line <- bufferedSource.getLines.drop(1)) {
//      val cols = line.split("\t").map(_.trim)
//      listType match {
//        case "bookings" => Booking(cols(0), cols(1), cols(2), cols(3), cols(4), cols(5), cols(6))
//        case "rooms" => RoomPrice(cols(0), cols(1))
//        case "charges" => RoomServiceCharges(cols(0), cols(1), cols(2), cols(3))
//        case "accounts" => Account(cols(0), cols(1), cols(2))
//      }
//    }
//    bufferedSource.close()
//  }

  def produceInvoice(account: Account, bookingList: List[Booking], roomPriceList: List[RoomPrice], roomServiceList: List[RoomServiceCharges]): AccountInvoice = {
    val accountBookings: List[Booking] = filterBookingsByAccountId(bookingList, account.accountId)
    val roomCost: Double = calculateRoomCost(accountBookings, roomPriceList)
    val discountedRoomCost: Double = applyDiscount(account.discount, roomCost)
    val accountRoomServiceCharges: List[RoomServiceCharges] = collateAccountCharges(accountBookings, roomServiceList)
    val roomServiceTotal: Double = calculateRoomServiceTotal(accountRoomServiceCharges)
    val total = discountedRoomCost + roomServiceTotal
    AccountInvoice(account, discountedRoomCost, roomServiceTotal, total)
  }

}
