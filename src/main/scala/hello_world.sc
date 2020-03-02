val list = List("Nikki", "Dan", "Dave") //Create list of names
def sayHello(name: String): Unit = {
  if (name == "Dan") {
    println("Hello again " + name)
  } else {
    println("Hello " + name)
  }
}
list.map(sayHello)