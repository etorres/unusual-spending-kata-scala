package es.eriktorr.katas

final class UnusualSpendingMonitor(
  calendar: Calendar,
  paymentsFetcher: PaymentsFetcher,
  alertSender: AlertSender
) {
  def triggerAlertFor(user: User): Unit = {
    val today = calendar.today()
    val oneMonthAgo = today.minusMonths(1L)
    val twoMonthsAgo = today.minusMonths(2L)

    def monthlyExpenses(payments: Seq[Payment]): (Double, Double) = {
      val paymentsByMonth =
        payments.groupBy(_.date.isBefore(oneMonthAgo)).map {
          case (m, ps) => (m, ps.map(_.price).sum)
        }
      (
        paymentsByMonth.getOrElse(true, 0L),
        paymentsByMonth.getOrElse(false, 0L)
      )
    }

    val paymentsByCategory =
      paymentsFetcher
        .paymentsFor(user, twoMonthsAgo, today)
        .groupBy(_.category)
        .map {
          case (category, payments) => (category, monthlyExpenses(payments))
        }
        .filter { case (_, (pastPayments, currentPayments)) => currentPayments > pastPayments }
        .map { case (category, (_, currentPayments)) => (category, currentPayments) }

    if (paymentsByCategory.nonEmpty) alertSender.alert(user, paymentsByCategory)
  }
}
