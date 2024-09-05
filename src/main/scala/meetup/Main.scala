package meetup

import scala.scalajs.js
import org.scalajs.dom
import com.raquo.laminar.api.L.{*, given}
import domain.Courier
import derivation.View
import domain.{Password, VehicleType}
import derivation.Mode

object Main:

  def main(args: Array[String]): Unit =
    val nameVar = Var(initial = "")
    val courier = Courier(
      name = "Jim",
      password = Password("mysecret"),
      vehicleType = VehicleType.Bicycle,
    )
    lazy val fleetCourier = Var(courier)
    val app = {
      div(
        div(
          h2("Fleet Courier Form"),
          View.derived[Courier].render(fleetCourier)(Mode.EDIT),
          h3("Output:"),
          child.text <-- fleetCourier.signal.map(_.toString),
        ),
      )
    }
    render(dom.document.getElementById("app"), app)



