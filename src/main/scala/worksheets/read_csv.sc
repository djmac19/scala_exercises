case class RoomPrice(roomTypeId: Int, cost: Double)
case class Person(personId: Int, firstName:String, surname:String )
case class RoomServiceCharges(bookingId: Int, itemName: String, cost:Double, date:String)
case class Booking(bookingId: Int, accountId: Int, personId: Int, room: String, roomTypeId: Int, startDate: String, daysBooked: Int)
case class Account(accountId: Int, accountName: String, discount:Int)

def readCSV(listType: String): List[Any] = {
  val bufferedSource = io.Source.fromFile(s"/Users/daniel.james.mcentee/Documents/scala_exercises/resources/${listType}.csv")
  //  for (line <- bufferedSource.getLines.drop(1)) {
  bufferedSource.getLines.drop(1).toList.map(line => {
    val cols = line.split(",").map(_.trim)
//    println(s"${cols(0)}|${cols(1)}|${cols(2)}|${cols(3)}|${cols(4)}|${cols(5)}|${cols(6)}")
    listType match {
        case "bookings" => Booking(cols(0).toInt, cols(1).toInt, cols(2).toInt, cols(3), cols(4).toInt, cols(5), cols(6).toInt)
        case "rooms" => RoomPrice(cols(0).toInt, cols(1).toDouble)
        case "charges" => RoomServiceCharges(cols(0).toInt, cols(1), cols(2).toDouble, cols(3))
        case "accounts" => Account(cols(0).toInt, cols(1), cols(2).toInt)
    }
  })
  bufferedSource.close()
}

readCSV("bookings")