package es.eriktorr.katas

import java.time.LocalDate

import org.scalacheck._

object UnusualSpendingMonitorSpec
    extends Properties("UnusualSpendingMonitorSpec")
    with DataGeneratorsSpec {
  import Prop.forAll
  import Prop.propBoolean

  property("triggerAlertOnUnusuallyHighSpending") = forAll(wastefulPaymentsGen) { payments =>
    payments.nonEmpty ==> triggeredAlertFor(wastefulUser, payments).nonEmpty
  }

  property("dontTriggerAlertOnNormalSpending") = forAll(sparingPaymentsGen) { payments =>
    triggeredAlertFor(sparingUser, payments).isEmpty
  }

  def triggeredAlertFor(user: User, payments: Seq[Payment]): Option[Alert] = {
    val alertSender = new SilentAlertSender()

    val unusualSpendingMonitor =
      new UnusualSpendingMonitor(
        new FixedCalendar(august31st2019),
        new InMemoryPaymentsFetcher(payments),
        alertSender
      )

    unusualSpendingMonitor.triggerAlertFor(user)

    alertSender.alert
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

  final class SilentAlertSender extends AlertSender {
    var alert: Option[Alert] = None

    override def alert(user: User, spending: Map[Category, Double]): Unit =
      alert = Some(formattedAlertFrom(spending))
  }
}
