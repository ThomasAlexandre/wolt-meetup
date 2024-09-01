package meetup

import scala.scalajs.js
import org.scalajs.dom
import com.raquo.laminar.api.L.{*, given}

def main(args: Array[String]): Unit = {
  println("Hello stockholm!")

  val nameVar = Var(initial = "world")
  val app = {
    div(
      label(b("Your name: ")),
      input(
        placeholder := "Enter your name here",
        onInput.mapToValue --> nameVar
      ),
      p(
        "Hello, ",
        child <-- nameVar.signal.map(_.toUpperCase)
      )
    )
  }

  render(dom.document.getElementById("app"), app)
}


