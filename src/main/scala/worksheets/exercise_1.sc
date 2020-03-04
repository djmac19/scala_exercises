case class Vehicle (vehicleId: Int, lat: Double, long: Double, model: String) {
  def getState: String = {
    if (lat < 104 && long > 100.01) {
      "Texas"
    } else if (lat > 104 && long < 105) {
      "San Diego"
    } else {
      "unknown"
    }
  }
}

case class VehicleEnriched (vehicleId: Int, lat: Double, long: Double, model: String, state: String)

val vehicle_1 = new Vehicle (1, 99.01, 100.01, "Model car 1")
val vehicle_2 = new Vehicle (2, 101.02, 100.01, "Model car 1")
val vehicle_3 = new Vehicle (3, 102, 100.02, "Model car 1")
val vehicle_4 = new Vehicle (4, 103.03, 100.04, "Model car 1")
val vehicle_5 = new Vehicle (5, 104.01, 100.01, "Model car 1")
val vehicle_6 = new Vehicle (6, 105.02, 100.01, "Model car 6")
val vehicle_7 = new Vehicle (7, 106.02, 100.01, "Model car 7")
val vehicle_8 = new Vehicle (8, 89, 100.02, "Model car 8")
val vehicle_9 = new Vehicle (9, 108.08, 100.01, "Model car 9")
val vehicle_10 = new Vehicle (10, 109, 106.01, "Model car 10")

val vehicleList = List(vehicle_1, vehicle_2, vehicle_3, vehicle_4, vehicle_5, vehicle_6, vehicle_7, vehicle_8, vehicle_9, vehicle_10)

val vehicleEnrichedList =
  vehicleList.map(vehicle => new VehicleEnriched(vehicle.vehicleId, vehicle.lat, vehicle.long, vehicle.model, vehicle.getState))