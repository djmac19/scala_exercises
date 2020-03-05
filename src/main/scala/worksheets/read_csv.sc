import scala.io.Source.{fromFile}
import scala.io.BufferedSource

sealed trait Chargeable

case class RoomPrice(roomTypeId: Int, cost: Double) extends Chargeable
case class Person(personId: Int, firstName:String, surname:String ) extends Chargeable
case class RoomServiceCharges(bookingId: Int, itemName: String, cost:Double, date:String) extends Chargeable
case class Booking(bookingId: Int, accountId: Int, personId: Int, room: String, roomTypeId: Int, startDate: String, daysBooked: Int) extends Chargeable
case class Account(accountId: Int, accountName: String, discount:Int) extends Chargeable

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

readCSV("bookings")
readCSV("rooms")
readCSV("charges")



//def readBookingsCSV(): List[Booking] = {
//  val bufferedSource = io.Source.fromFile(s"/Users/daniel.james.mcentee/Documents/scala_exercises/resources/bookings.csv")
//  //  for (line <- bufferedSource.getLines.drop(1)) {
//  val result = bufferedSource.getLines.drop(1).toList.map(line => {
//    val cols = line.split(",").map(_.trim)
//    //    println(s"${cols(0)}|${cols(1)}|${cols(2)}|${cols(3)}|${cols(4)}|${cols(5)}|${cols(6)}")
//    Booking(cols(0).toInt, cols(1).toInt, cols(2).toInt, cols(3), cols(4).toInt, cols(5), cols(6).toInt)
//  })
//  bufferedSource.close()
//  result
//}
//
//readBookingsCSV()