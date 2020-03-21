package es.eriktorr.katas

sealed abstract class Category(val displayName: String)

case object Entertainment extends Category("entertainment")
case object Restaurants extends Category("restaurants")
case object Golf extends Category("golf")
case object Groceries extends Category("groceries")
case object Travel extends Category("travel")
