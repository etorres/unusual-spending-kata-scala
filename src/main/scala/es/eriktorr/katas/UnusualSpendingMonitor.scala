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

    def monthlySpendingFrom(payments: Seq[Payment]): (Double, Double) = {
      val paymentsByMonth =
        payments.groupBy(_.date.isBefore(oneMonthAgo)).map {
          case (isItFromLastMonth, ps) => (isItFromLastMonth, ps.map(_.price).sum)
        }
      (
        paymentsByMonth.getOrElse(true, 0L),
        paymentsByMonth.getOrElse(false, 0L)
      )
    }

    val inlineMe = paymentsFetcher // TODO
      .paymentsFor(user, twoMonthsAgo, today)

    val spendingByCategory =
      inlineMe
        .groupBy(_.category)
        .map {
          case (category, payments) => (category, monthlySpendingFrom(payments))
        }
        .filter {
          case (_, (pastMonthSpending, currentMonthSpending)) =>
            currentMonthSpending > pastMonthSpending
        }
        .map { case (category, (_, currentMonthSpending)) => (category, currentMonthSpending) }

    // TODO
    println(
      "\n\n >> HERE: payments=" + inlineMe.toString() + "\n spending=" + spendingByCategory
        .toString() + "\n"
    )
    // TODO

    if (spendingByCategory.nonEmpty) alertSender.alert(user, spendingByCategory)
  }
}
