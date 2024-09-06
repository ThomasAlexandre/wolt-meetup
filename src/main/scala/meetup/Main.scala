package meetup

import org.scalajs.dom
import com.raquo.laminar.api.L.{*, given}
import typings.chartJs.mod.{^ as Chart, *}
import scala.scalajs.js
import scala.scalajs.js.|
import js.JSConverters._
import domain.{CalculatedEmission, EmissionData}
import typings.chartJs.mod.{^ as Chart, *}
import squants.mass.Tonnes
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("chart.js", JSImport.Default)
class Chart(ctx: js.Any, options: ChartConfiguration) extends js.Object

object Chart {
  def apply(ctx: js.Dynamic, chartConfiguration: ChartConfiguration): typings.chartJs.mod.Chart = new Chart(
    ctx,
    chartConfiguration
  ).asInstanceOf[typings.chartJs.mod.Chart]
}

def main(args: Array[String]): Unit =
  val Labels: scala.scalajs.js.Array[String | scala.scalajs.js.Array[scala.scalajs.js.Date | Double | typings.moment.mod.Moment | String] | Double | scala.scalajs.js.Date | typings.moment.mod.Moment] =
    js.Array("Week1", "Week2", "Week3", "Week4")

  def color(r: Int, g: Int, b: Int, a: Double): String =
    s"rgba($r, $g, $b, $a)"

  val BackgroundColor: ChartColor =
    js.Array(
      color(54, 162, 235, 0.2),
      color(255, 206, 86, 0.2),
      color(75, 192, 192, 0.2),
      color(153, 102, 255, 0.2),
      color(255, 159, 64, 0.2)
    )

  val BorderColor: ChartColor =
    js.Array(
      color(255, 99, 132, 1),
      color(54, 162, 235, 1),
      color(255, 206, 86, 1),
      color(75, 192, 192, 1),
      color(153, 102, 255, 1),
      color(255, 159, 64, 1)
    )

  def chartConfig(tpe: ChartType, Data: js.Array[js.UndefOr[ChartPoint | Double | Null]]): ChartConfiguration =
    ChartConfiguration()
      .setType(tpe)
      .setData(
        ChartData()
          .setLabels(Labels)
          .setDatasets(
            js.Array(
              ChartDataSets()
                .setLabel("CO2 Level")
                .setData(Data.asInstanceOf[js.Array[js.UndefOr[js.Array[Double] | typings.chartJs.mod.ChartPoint | Double | Null]]])
                .setBorderWidth(1)
                .setBackgroundColor(BackgroundColor)
                .setBorderColor(BorderColor)
            )
          )
      )
      .setOptions(ChartOptions().setResponsive(true))

  val app = div(
    h4("Environmental Report"),
    canvas(onMountCallback { nodeCtx =>
      val ctx: dom.Element = nodeCtx.thisNode.ref
      val sampleEmissionRecords: Seq[CalculatedEmission] = EmissionData.calculatedEmissions
      val cO2Data: js.Array[js.UndefOr[ChartPoint | Double | scala.Null]] =
        sampleEmissionRecords.map(record => record.cO2.to(Tonnes)).toJSArray
      new Chart(ctx, chartConfig(ChartType.line, cO2Data))
    })
  )

  render(dom.document.getElementById("app"), app)


