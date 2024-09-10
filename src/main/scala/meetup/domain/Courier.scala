package domain

import java.time.LocalDate

enum VehicleType:
  case Bicycle, Car, Motorcycle

case class Password(underlying: String)

case class Courier(name: String,
                   password: Password,
                   vehicleType: VehicleType,
                   //canBookShifts: Boolean = false
                  )



