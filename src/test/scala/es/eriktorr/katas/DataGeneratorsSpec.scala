package es.eriktorr.katas

import java.time.LocalDate

import org.scalacheck.Gen
import org.scalacheck.util.Buildable

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

trait DataGeneratorsSpec {
  val sparingUser: User = User("sparing_user")
  val wastefulUser: User = User("wasteful_user")

  val august31st2019: LocalDate = LocalDate.of(2019, 8, 31)
  val firstDayOfThePeriod: LocalDate = august31st2019.minusMonths(1L)

  val dateGen: Gen[LocalDate] = {
    val rangeStart = august31st2019.minusMonths(2L).toEpochDay
    val rangeEnd = august31st2019.toEpochDay
    Gen
      .choose(rangeStart, rangeEnd)
      .map(i => LocalDate.ofEpochDay(i))
  }

  val priceGen: (User, LocalDate) => Gen[Double] = (user, date) =>
    user match {
      case x if x == sparingUser =>
        if (date.isBefore(firstDayOfThePeriod)) Gen.choose(501.0, 1000.0)
        else Gen.choose(1.0, 500.0)
      case _ => Gen.choose(1.0, 1000.0)
    }

  val paymentGen: User => Gen[Payment] = user =>
    for {
      date <- dateGen
      price <- priceGen(user, date)
      description <- Gen.alphaStr
      category <- Gen.oneOf(Entertainment, Restaurants, Golf, Groceries, Travel)
    } yield Payment(date = date, price = price, description = description, category = category)

  implicit def buildableArrayBuffer[T]: Buildable[T, mutable.ArrayBuffer[T]] =
    new Buildable[T, mutable.ArrayBuffer[T]] {
      override def builder: mutable.Builder[T, ArrayBuffer[T]] = ArrayBuffer.newBuilder[T]
    }

  val paymentsGen: Gen[Payment] => Gen[Seq[Payment]] = paymentGenerator =>
    for {
      payments <- Gen.containerOf[mutable.ArrayBuffer, Payment](paymentGenerator)
    } yield payments.toSeq

  val sparingPaymentsGen: Gen[Seq[Payment]] = paymentsGen(paymentGen(sparingUser))
  val wastefulPaymentsGen: Gen[Seq[Payment]] = paymentsGen(paymentGen(wastefulUser))
}
