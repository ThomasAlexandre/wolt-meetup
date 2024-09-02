package derivation

import com.raquo.laminar.api.L.*

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.compiletime.*
import scala.deriving.*
import domain.VehicleType
import domain.Password

import scala.Tuple.{Union, fromArray}

enum Mode:
  case READ, EDIT

enum MyOption[+T]:
  case MyNone
  case MySome(x: T)

  def isDefined: Boolean = this match
    case MyNone => false
    case MySome(_) => true

object MyOption:
  def apply[T >: Null](x: T): MyOption[T] =
    if (x == null) MyNone else MySome(x)

trait View[T] {
  self =>

  def labelled(str: String): View[T] = new View[T] {
    override def renderImpl(variable: Var[T])(implicit owner: Owner, mode: Mode = Mode.EDIT): HtmlElement =
      div(
        cls("input-group"),
        label(str),
        self.renderImpl(variable)
      )
  }

  def renderImpl(variable: Var[T])(implicit owner: Owner, mode: Mode): Mod[HtmlElement]

  def render(variable: Var[T])(implicit mode: Mode): HtmlElement =
    mode match
      case Mode.EDIT => form(
        onMountInsert { ctx =>
          div(renderImpl(variable)(ctx.owner, mode))
        })
      case Mode.READ => div(
        onMountInsert { ctx =>
          div(renderImpl(variable)(ctx.owner, mode))
        })

  def ~[B](that: View[B]): View[(T, B)] = new View[(T, B)] {
    override def renderImpl(variable: Var[(T, B)])(implicit owner: Owner, mode: Mode): Mod[HtmlElement] =
      Seq(
        self.renderImpl(variable.zoom(x => x._1)(_ -> variable.now()._2))(owner, mode),
        that.renderImpl(variable.zoom(_._2)(variable.now()._1 -> _))(owner, mode)
      )
  }

  def xmap[B](to: T => B)(from: B => T): View[B] = new View[B] {
    override def renderImpl(variable: Var[B])(implicit owner: Owner, mode: Mode): Mod[HtmlElement] =
      self.renderImpl(variable.zoom[T](from)(to))(owner, mode)
  }
}

object View {

  given stringView: View[String] with
    def renderImpl(variable: Var[String])(implicit owner: Owner, mode: Mode): HtmlElement =
      mode match
        case Mode.EDIT =>
          input(
            controlled(
              value <-- variable,
              onInput.mapToValue --> variable
            )
          )
        case Mode.READ =>
          p(
            value <-- variable
          )

  given passwordView: View[Password] with
    def renderImpl(variable: Var[Password])(implicit owner: Owner, mode: Mode): HtmlElement = {
      input(
        typ := "password",
        value <-- variable.signal.map(_.underlying),
        inContext { thisNode => onInput.mapTo(Password(thisNode.ref.value)) --> variable }
      )
    }

  given dateView: View[LocalDate] with
    def renderImpl(variable: Var[LocalDate])(implicit owner: Owner, mode: Mode): HtmlElement = {
      val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", java.util.Locale.getDefault)
      input(
        typ := "date", // "datetime-local"
        value <-- variable.signal.map(localDate => localDate.format(formatter)),
        inContext { thisNode => onInput.mapTo(LocalDate.parse(thisNode.ref.value, formatter)) --> variable }
      )
    }

  given intForm: View[Int] = stringView.xmap(x => x.toInt)(_.toString)

  given doubleForm: View[Double] = stringView.xmap(x => x.toDouble)(_.toString)

  given booleanView: View[Boolean] with
    def renderImpl(variable: Var[Boolean])(implicit owner: Owner, mode: Mode): HtmlElement = {
      //val varBus = new EventBus[Boolean]
      input(
        cls("toggle"),
        typ := "checkbox",
        checked <-- variable,
        inContext { thisNode => onClick.mapTo(thisNode.ref.checked) --> variable }
      )
    }

  given vehicleTypeView: View[VehicleType] with
    def renderImpl(variable: Var[VehicleType])(implicit owner: Owner, mode: Mode): HtmlElement = {
      //val allowedIcons = List("🎉", "🚀", "🍉")
      val allowedValues = VehicleType.values.toList.map(_.toString)
      val iconVar = Var(initial = allowedValues(variable.now().ordinal))
      mode match
        case Mode.EDIT =>
          select(
            inContext { thisNode =>
              onChange.mapTo(thisNode.ref.value) --> iconVar.writer
            },
            value <-- iconVar.signal,
            allowedValues.map(icon => option(value(icon), icon))
          )
        case Mode.READ =>
          input(
            value <-- variable.signal.map(ft => ft match
              case VehicleType.Bicycle => "Bicycle"
              case VehicleType.Car => "Car"
              case VehicleType.Motorcycle => "Motorcycle"
            ),
            // inContext { thisNode => onClick.mapTo(thisNode.ref.textContent) --> variable }
          )
    }

  def iterator[T](p: T): Iterator[Any] = p.asInstanceOf[Product].productIterator

  def unapplySeq(s: String): Option[Seq[Char]] = Some(s.toList)

  def recurseSum[Types <: Tuple, T](element: T): HtmlElement =
    element match
      case something: (tpe *: types) =>
        println(s"Sum element is: $element")
        if (element.isInstanceOf[tpe])
          println(s"Element $element is instance of $something")
          //summonInline[tpe].renderImpl(element.asInstanceOf[tpe])
          ???
        else
          recurseSum[types, T](element)
      case _: EmptyTuple => throw new IllegalArgumentException(s"Invalid co-product type")

  def recurseMatch[T](tup: T): List[Any] = tup match
    case t *: ts *: EmptyTuple => ts :: recurseMatch(t)
    case x => x :: Nil

  def viewSum[B](s: Mirror.SumOf[B], views: => List[View[?]]): View[B] =
    new View[B]:
      def renderImpl(variable: Var[B])(implicit owner: Owner, mode: Mode): HtmlElement =
        println(s"Sum Elems: $views")
        val index = s.ordinal(variable.now())
        println(s"viewSum index: $index")
        val current: View[?] = views(index)
        println(s"current elem: $current")
        recurseSum(current)
  /*
  type RecursiveTupleType = Tuple.Fold[s.MirroredElemTypes, EmptyTuple, Tuple2]
  val reducedElems = views.reduceLeft(_ ~ _).asInstanceOf[View[RecursiveTupleType]]
  reducedElems.xmap[B] { case tuple =>
    println(s"Sum Current tuple: $tuple")
    val result = recurseSum[s.MirroredElemTypes, B](variable.now())
    ???
  }{ (sumType: B) => sumType match {
    case Some(value) =>
      println(s"case Some $value")
      val caseProductIterator: Seq[Any] = View.iterator(value).toList
      val output = caseProductIterator.reduceLeft((x, y) => x -> y).asInstanceOf[RecursiveTupleType]
      println(s"case Some inre product Output: $output")
      output
    case _ => EmptyTuple.asInstanceOf[RecursiveTupleType]
    }
  }.render(variable)
  */

  def viewProduct[B](m: Mirror.ProductOf[B], views: => List[View[?]]): View[B] =
    new View[B]:
      def renderImpl(variable: Var[B])(implicit owner: Owner, mode: Mode): HtmlElement =
        type RecursiveTupleType = Tuple.Fold[m.MirroredElemTypes, EmptyTuple, Tuple2]

        def recurseMatch[T](tup: T): List[Any] = tup match
          case t *: ts *: EmptyTuple => ts :: recurseMatch(t)
          case x => x :: Nil

        println(s"Product Elems: $views")
        div {
          val reducedElems = views.reduceLeft(_ ~ _).asInstanceOf[View[RecursiveTupleType]]
          reducedElems.xmap[B] { case tuple =>
            println(s"Product Current tuple: $tuple")
            val result: Array[Any] = recurseMatch(tuple).reverse.toArray
            val paramsTuple: Tuple = Tuple.fromArray(result)
            println(s"Product ParamsTuple: $paramsTuple")
            m.fromProduct(paramsTuple)
          } { case caseClass: B => // B is a productType ie case class Person(name, address, age)
            println(s"Product Current caseclas: $caseClass")
            val output = View.iterator(caseClass).reduceLeft((x, y) => x -> y).asInstanceOf[RecursiveTupleType]
            println(s"Product Output: $output")
            output
          }.render(variable)
        }

  inline given derived[T](using m: Mirror.Of[T]): View[T] =
    lazy val elemInstances = summonAll[m.MirroredElemTypes]
    val labels = constValueTuple[m.MirroredElemLabels].toList.asInstanceOf[List[String]]
    println(s"Deriving Labels: $labels")
    val labelledViews = labels.zip(elemInstances).map { case (label, elemForm) =>
      elemForm.labelled(label)
    }
    println(s"Deriving Labelled views: $labelledViews")
    inline m match
      case s: Mirror.SumOf[T] =>
        viewSum(s, labelledViews)
      case p: Mirror.ProductOf[T] =>
        viewProduct(p, labelledViews)

  inline def summonAll[T <: Tuple]: List[View[?]] =
    inline erasedValue[T] match
      case _: EmptyTuple => Nil
      case _: (t *: ts) => summonInline[View[t]] :: summonAll[ts]

}




