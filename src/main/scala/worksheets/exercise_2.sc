import scala.io.BufferedSource
import scala.io.Source.fromFile
import java.nio.file.{Path, Paths, Files}

sealed trait Chargeable

final case class RoomPrice(roomTypeId: Int, cost: Double) extends Chargeable
final case class Person(personId: Int, firstName:String, surname:String ) extends Chargeable
final case class RoomServiceCharges(bookingId: Int, itemName: String, cost:Double, date:String) extends Chargeable
final case class Booking(bookingId: Int, accountId: Int, personId: Int, room: String, roomTypeId: Int, startDate: String, daysBooked: Int) extends Chargeable
final case class Account(accountId: Int, accountName: String, discount:Int) extends Chargeable

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

def readCSV(fileName: String): List[Chargeable] = {
  val bufferedSource: BufferedSource = fromFile(s"/Users/daniel.james.mcentee/Documents/scala_exercises/src/main/resources/${fileName}.csv")
  val result = bufferedSource.getLines.drop(1).toList.map(line => {
    val cols = line.split(",").map(_.trim)
    fileName match {
      case "bookings" => Booking(cols(0).toInt, cols(1).toInt, cols(2).toInt, cols(3), cols(4).toInt, cols(5), cols(6).toInt)
      case "rooms" => RoomPrice(cols(0).toInt, cols(1).toDouble)
      case "charges" => RoomServiceCharges(cols(0).toInt, cols(1), cols(2).toDouble, cols(3))
      case "accounts" => Account(cols(0).toInt, cols(1), cols(2).toInt)
    }
  })
  bufferedSource.close()
  result
}

def writeCSV(invoiceList: List[AccountInvoice]): Unit = {
  val path: Path = Paths.get("/Users/daniel.james.mcentee/Documents/scala_exercises/src/main/resources/invoices.csv")
  val headerString: String = "accountId, accountName, discount, roomCost, roomServiceTotal, total\n"
  val dataString: String = invoiceList
    .map(invoice => {
      val list = List(invoice.account.accountId, invoice.account.accountName, invoice.account.discount, invoice.roomCost, invoice.roomServiceTotal, invoice.total)
      list.mkString("", ", ", "\n")
    })
    .foldLeft("")((concat, str) => concat + str)
  val CSVString = headerString + dataString
  Files.write(path, CSVString.getBytes())
}

def produceInvoices(): List[AccountInvoice] = {

  val accountList: List[Account] = readCSV("accounts").asInstanceOf[List[Account]]
  val bookingList: List[Booking] = readCSV("bookings").asInstanceOf[List[Booking]]
  val roomPriceList: List[RoomPrice] = readCSV("rooms").asInstanceOf[List[RoomPrice]]
  val roomServiceList: List[RoomServiceCharges] = readCSV("charges").asInstanceOf[List[RoomServiceCharges]]

  val invoiceList = accountList.map(account => {
    val accountBookings: List[Booking] = filterBookingsByAccountId(bookingList, account.accountId)
    val roomCost: Double = calculateRoomCost(accountBookings, roomPriceList)
    val discountedRoomCost: Double = applyDiscount(account.discount, roomCost)
    val accountRoomServiceCharges: List[RoomServiceCharges] = collateAccountCharges(accountBookings, roomServiceList)
    val roomServiceTotal: Double = calculateRoomServiceTotal(accountRoomServiceCharges)
    val total = discountedRoomCost + roomServiceTotal
    AccountInvoice(account, discountedRoomCost, roomServiceTotal, total)
  })
  writeCSV(invoiceList)
  invoiceList
}

produceInvoices()