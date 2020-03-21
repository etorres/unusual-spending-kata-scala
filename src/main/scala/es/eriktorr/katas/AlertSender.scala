package es.eriktorr.katas

trait AlertSender {
  def alert(user: User, spending: Map[Category, Double]): Unit

  final def formattedAlertFrom(spending: Map[Category, Double]): Alert = Alert(
    s"Unusual spending of $$${totalAmountFrom(spending)} detected!",
    s"""
       |Hello card user!
       |
       |We have detected unusually high spending on your card in these categories:
       |
       |${formattedItemFrom(spending)}
       |
       |Love,
       |
       |The Credit Card Company
       |""".stripMargin
  )

  final def totalAmountFrom(spending: Map[Category, Double]): String = rounded(spending.values.sum)

  final def formattedItemFrom(spending: Map[Category, Double]): String =
    spending
      .map {
        case (category, amount) => s"* You spent $$${rounded(amount)} on ${category.displayName}"
      }
      .mkString("\n")

  final def rounded(amount: Double): String = f"$amount%1.0f"
}
