package es.eriktorr.katas

import java.time.LocalDate

import org.scalacheck.Gen
import org.scalacheck.util.Buildable

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

trait DataGeneratorsSpec {
  val user1: User = User("user_1")

  val march16th2020: LocalDate = LocalDate.of(2020, 8, 16)

  val dateGen: Gen[LocalDate] = {
    val rangeStart = march16th2020.minusMonths(1L).toEpochDay
    val rangeEnd = march16th2020.toEpochDay
    Gen
      .choose(rangeStart, rangeEnd)
      .map(i => LocalDate.ofEpochDay(i))
  }

  val paymentGen: Gen[Payment] =
    for {
      date <- dateGen
      price <- Gen.choose(1.0, 1000.0)
      description <- Gen.asciiPrintableStr
      category <- Gen.oneOf(Entertainment, Restaurants, Golf, Groceries, Travel)
    } yield Payment(date = date, price = price, description = description, category = category)

  implicit def buildableArrayBuffer[T]: Buildable[T, mutable.ArrayBuffer[T]] =
    new Buildable[T, mutable.ArrayBuffer[T]] {
      override def builder: mutable.Builder[T, ArrayBuffer[T]] = ArrayBuffer.newBuilder[T]
    }

  val paymentsGen: Gen[Seq[Payment]] = for {
    payments <- Gen.containerOf[mutable.ArrayBuffer, Payment](paymentGen)
  } yield payments.toSeq
}
