package alex_exercises

import java.util.{Date, UUID}

object Main extends App {

  case class User(
      firstName: String,
      lastName: String,
      age: Int,
      dob: Date,
      userId: UUID,
      metadata: Option[String]
  )
  case class StockItem(
      name: String,
      price: Double,
      description: Option[String],
      rating: Option[Int]
  ) {
    def f():String = s"$name  ${price.toString}"
  }

  // in memory stock store
  case class Stock(
      stock: Option[List[StockItem]]
  )
  case class Products(
      products: Option[List[Stock]]
  )

  val eggs = StockItem(
    "eggs",
    1.99d,
    None,
    None
  )
  eggs.f

  Map()


  object StockItem {

  }


}
