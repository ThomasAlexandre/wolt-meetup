package meetup

import cats.effect.IO
import tyrian.*

object Main {
  def main(args: Array[String]): Unit =
    TyrianIOApp.onLoad(
      "CounterApp" -> CounterApp
    )
}