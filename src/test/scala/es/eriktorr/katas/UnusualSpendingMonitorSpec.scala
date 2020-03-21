package es.eriktorr.katas

import java.time.LocalDate

import org.scalacheck._

object UnusualSpendingMonitorSpec extends Properties("UnusualSpendingMonitorSpec") {
  import Prop.forAll

  private val March16th2020 = LocalDate.of(2020, 3, 16)
  private val TargetUser = User("user_123")

  property("triggerAlertOnUnusuallyHighSpending") = forAll { input: Int =>
    val unusualSpendingMonitor =
      new UnusualSpendingMonitor(
        new FixedClock(March16th2020),
        new InMemoryPaymentsFetcher(Map.empty),
        new MemoryAlertSender()
      )

    unusualSpendingMonitor.triggerAlertFor(TargetUser)

    true
  }

  final class FixedClock(fixedDate: LocalDate) extends Clock {
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
    private[this] var alert: Alert = Alert("__undef__", "__undef__")

    override def alert(user: User, spending: Seq[Spending]): Unit =
      alert = formattedAlertFrom(spending)
  }
}
