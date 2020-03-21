package es.eriktorr.katas

import java.time.LocalDate

final case class Payment(date: LocalDate, price: Double, description: String, category: Category)
