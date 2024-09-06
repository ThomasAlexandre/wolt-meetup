package domain

import java.time.temporal.TemporalAmount
import java.time.{Duration, Instant, Period, temporal}
import java.util.UUID
import squants.mass.{Mass, Tonnes}

case class CalculatedEmission(hc: Mass, co: Mass, cO2: Mass)

object EmissionData {

  val calculatedEmissions = List(
    CalculatedEmission(
      hc = Tonnes(0.3),
      co = Tonnes(0.1),
      cO2 = Tonnes(2.7),
    ),
    CalculatedEmission(
      hc = Tonnes(0.023),
      co = Tonnes(0.021),
      cO2 = Tonnes(2.4),
    ),
    CalculatedEmission(
      hc = Tonnes(0.023),
      co = Tonnes(0.021),
      cO2 = Tonnes(2.6),
    ),
    CalculatedEmission(
      hc = Tonnes(0.023),
      co = Tonnes(0.021),
      cO2 = Tonnes(2.3),
    )
  )
}