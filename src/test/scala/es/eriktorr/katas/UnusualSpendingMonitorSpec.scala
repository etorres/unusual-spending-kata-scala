package es.eriktorr.katas

import java.time.LocalDate

import org.scalacheck._

object UnusualSpendingMonitorSpec
    extends Properties("UnusualSpendingMonitorSpec")
    with DataGeneratorsSpec {
  import Prop.forAll

  property("triggerAlertOnUnusuallyHighSpending") = forAll(paymentsGen) { payments =>
    val unusualSpendingMonitor =
      new UnusualSpendingMonitor(
        new FixedCalendar(march16th2020),
        new InMemoryPaymentsFetcher(payments),
        new MemoryAlertSender()
      )

    unusualSpendingMonitor.triggerAlertFor(user1)

    true // TODO
  }

  final class FixedCalendar(fixedDate: LocalDate) extends Calendar {
    override def today(): LocalDate = fixedDate
  }

  final class InMemoryPaymentsFetcher(payments: Seq[Payment]) extends PaymentsFetcher {
    override def paymentsFor(
      user: User,
      startDate: LocalDate,
      endDate: LocalDate
    ): Seq[Payment] = payments
  }

  final class MemoryAlertSender extends AlertSender {
    private[this] var alert: Option[Alert] = None

    override def alert(user: User, spending: Map[Category, Double]): Unit =
      alert = Some(formattedAlertFrom(spending))
  }
}
