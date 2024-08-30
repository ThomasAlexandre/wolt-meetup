package meetup

import scala.scalajs.js
import org.scalajs.dom

@main def hello(): Unit =
  dom.document.querySelector("#app").innerHTML =
    """
    <h1>Hello, Wolt Scala Meetup!</h1>
    """
