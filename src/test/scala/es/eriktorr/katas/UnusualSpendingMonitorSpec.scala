package es.eriktorr.katas

import java.time.LocalDate

import org.scalacheck._

object UnusualSpendingMonitorSpec
    extends Properties("UnusualSpendingMonitorSpec")
    with DataGeneratorsSpec {
  import Prop.forAll

  property("triggerAlertOnUnusuallyHighSpending") = forAll(spendingGen) {
    case (targetUser, userPayments) =>
      val unusualSpendingMonitor =
        new UnusualSpendingMonitor(
          new FixedCalendar(march16th2020),
          new InMemoryPaymentsFetcher(userPayments),
          new MemoryAlertSender()
        )

      unusualSpendingMonitor.triggerAlertFor(targetUser)

      true
  }

  final class FixedCalendar(fixedDate: LocalDate) extends Calendar {
    override def today(): LocalDate = fixedDate
  }

  final class InMemoryPaymentsFetcher(payments: Map[User, Seq[Payment]]) extends PaymentsFetcher {
    override def paymentsFor(
      user: User,
      startDate: LocalDate,
      endDate: LocalDate
    ): Seq[Payment] = payments.getOrElse(user, Seq.empty)
  }

  final class MemoryAlertSender extends AlertSender {
    private[this] var alert: Option[Alert] = None

    override def alert(user: User, spending: Seq[Spending]): Unit =
      alert = Some(formattedAlertFrom(spending))
  }
}
