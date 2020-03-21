package es.eriktorr.katas

trait AlertSender {
  def alert(user: User, spending: Seq[Spending]): Unit

  final def formattedAlertFrom(spending: Seq[Spending]): Alert = Alert(
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

  final def totalAmountFrom(spending: Seq[Spending]): String = rounded(spending.map(_.amount).sum)

  final def formattedItemFrom(spending: Seq[Spending]): String =
    spending
      .map(s => s"* You spent $$${rounded(s.amount)} on ${s.category.displayName}")
      .mkString("\n")

  final def rounded(amount: Double): String = f"$amount%1.0f"
}
