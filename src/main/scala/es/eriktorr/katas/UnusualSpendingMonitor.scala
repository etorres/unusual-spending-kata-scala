package es.eriktorr.katas

final class UnusualSpendingMonitor(
  calendar: Calendar,
  paymentsFetcher: PaymentsFetcher,
  alertSender: AlertSender
) {
  def triggerAlertFor(user: User): Unit = {
    // TODO
  }
}
