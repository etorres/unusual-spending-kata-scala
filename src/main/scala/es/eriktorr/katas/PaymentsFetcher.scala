package es.eriktorr.katas

import java.time.LocalDate

trait PaymentsFetcher {
  def paymentsFor(user: User, startDate: LocalDate, endDate: LocalDate): Seq[Payment]
}
