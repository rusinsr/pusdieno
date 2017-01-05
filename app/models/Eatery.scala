package models

import java.util.UUID

import slick.driver.PostgresDriver.api._

object EateryList {
  val eateries = List(
    Eatery("subway", "images/eateries/SUBWAY-SHOP.jpg", new OpenTimes(Array((0, 0), (0, 0), (0, 0), (0, 0), (0, 0), (830, 1730), (830, 1730)))),
    Eatery("kfc", "images/eateries/YUMB_00_KFC_large.jpg", new OpenTimes(Array((8000, 2100), (8000, 2100), (8000, 2100), (8000, 2100), (8000, 2100), (830, 1730), (830, 1730)))),
    Eatery("pelmeni", "", new OpenTimes(Array((8000, 2100), (8000, 2100), (8000, 2100), (8000, 2100), (8000, 2100), (830, 1730), (830, 1730)))),
    Eatery("pankukas", "", new OpenTimes(Array((8000, 2100), (8000, 2100), (8000, 2100), (8000, 2100), (8000, 2100), (830, 1730), (830, 1730))))
  )
}

case class Eatery(id: String, backgroundImage: String, openTimes: OpenTimes)

//case class Eatery(uuid: UUID = UUID.randomUUID, chain: String, typ: EateryType)

abstract class EateryTable(tag: Tag) extends Table(tag, "eateries") {
}