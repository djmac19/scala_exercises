//import java.io.{FileWriter, BufferedWriter}
//import au.com.bytecode.opencsv.CSVWriter
//import scala.jdk.CollectionConverters._
//import scala.collection.mutable.ListBuffer
//
//val out = new BufferedWriter(new FileWriter("/Users/daniel.james.mcentee/Documents/scala_exercises/resources/invoices.csv"))
//val writer = new CSVWriter(out)
//val invoiceSchema = Array("accountId", "accountName", "discount", "roomCost", "roomServiceTotal", "total")
//val invoice1 = Array("1", "BusyPeopleInc", "0" , "400.0", "0.0", "400.0")
//val invoice2 = Array("2", "Mr Blank Name", "50", "850.0", "51.45", "901.45")
//var listOfRecords = new ListBuffer[Array[String]]()
//listOfRecords = listOfRecords :+ invoiceSchema
//listOfRecords = listOfRecords :+ invoice1
//listOfRecords = listOfRecords :+ invoice2
////val listOfRecords = List(invoiceSchema, invoice1, invoice2).asJava
//writer.writeAll(listOfRecords.asJava)
//out.close()
//println("done")





//val invoiceSchema = List("accountId", "accountName", "discount", "roomCost", "roomServiceTotal", "total")
//val invoice1 = List(1, "BusyPeopleInc", 0 , 400.0, 0.0, 400.0)
//val invoice2 = List(2, "Mr Blank Name", 50, 850.0, 51.45, 901.45)
//
//val string: String = invoice1.mkString("", ", ", "\n")
//
//val path = Paths.get("/Users/daniel.james.mcentee/Documents/scala_exercises/src/main/resources/invoices.csv")
//
//Files.write(path, string.getBytes())





import java.nio.file.{Files, Path, Paths}
import dave_exercises.Main.{Account, AccountInvoice}

val invoice1: AccountInvoice = AccountInvoice(Account(1, "BusyPeopleInc" , 0), 400.0, 0.0, 400.0)
val invoice2: AccountInvoice = AccountInvoice(Account(2, "Mr Blank Name", 50), 850.0, 51.45, 901.45)
val invoiceList: List[AccountInvoice] = List(invoice1, invoice2)

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

writeCSV(invoiceList)







