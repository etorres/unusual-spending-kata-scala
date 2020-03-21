package es.eriktorr.katas

import java.time.LocalDate

trait Calendar {
  def today(): LocalDate = LocalDate.now()
}
