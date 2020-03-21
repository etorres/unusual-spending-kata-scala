package es.eriktorr.katas

final class UnusualSpendingMonitor(
  clock: Clock,
  paymentsFetcher: PaymentsFetcher,
  alertSender: AlertSender
) {
  def triggerAlertFor(user: User): Unit = ???
}
