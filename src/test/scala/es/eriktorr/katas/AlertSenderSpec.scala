package es.eriktorr.katas

class AlertSenderSpec extends UnitSpec {
  "Alert sender" should "create a list of formatted spending statements" in new SilentAlertSender() {
    formattedItemFrom(Seq(Spending(148.0, Groceries))) shouldBe "* You spent $148 on groceries"
  }

  "Alert sender" should "create a formatted alert message" in new SilentAlertSender() {
    formattedAlertFrom(Seq(Spending(148.0, Groceries), Spending(928.0, Travel))) shouldBe Alert(
      "Unusual spending of $1076 detected!",
      """
        |Hello card user!
        |
        |We have detected unusually high spending on your card in these categories:
        |
        |* You spent $148 on groceries
        |* You spent $928 on travel
        |
        |Love,
        |
        |The Credit Card Company
        |""".stripMargin
    )
  }

  abstract class SilentAlertSender extends AlertSender {
    override def alert(user: User, spending: Seq[Spending]): Unit = {}
  }
}
