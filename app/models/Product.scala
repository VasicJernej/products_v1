package models

case class Product(ean: Long, name: String, description: String)

object Product {
  val products = Set(
    Product(5010255079763L, "Paperclips Large", "Large Plain Pack of 1000"),
    Product(5018206244666L, "Giant paperclips", "Giant plain 51mm 100 pack"),
    Product(5018306332812L, "Paperclip Giant Plain", "Giant plain pack of 10000"),
    Product(5018306312913L, "No Tear Paper Clip", "No tear extra large pack of 1000"),
    Product(5018206244611L, "Zebra paperclips", "Zebra length 28mm assorted 150 pack")
  )
  def findAll = products.toList.sortBy(_.ean)
}