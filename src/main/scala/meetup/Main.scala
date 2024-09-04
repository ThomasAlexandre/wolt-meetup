package meetup

import org.scalajs.dom
import com.raquo.laminar.api.L.{*, given}

def main(args: Array[String]): Unit =
  val nameVar = Var(initial = "")
  val app = div(
    label(b("Your name: ")),
    input(
      placeholder := "Enter your name ", onInput.mapToValue --> nameVar),
    p("Hello, ", child <-- nameVar.signal.map(_.toUpperCase))
  )
  render(dom.document.getElementById("app"), app)


