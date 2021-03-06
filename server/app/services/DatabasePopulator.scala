package services

import java.util.UUID
import javax.inject.{Inject, Singleton}

import models.{Chain, Restaurant, User}
import models.db._
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.daos.EstablishmentDAO
import slick.driver.JdbcProfile
import slick.jdbc.PostgresProfile.api._
import utils.LoggingSupport

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
  * This class is meant for quickly populating the database in case I wipe everything.
  * ONLY USE FOR TESTING PURPOSES!!!
  */
@Singleton
class DatabasePopulator @Inject()(restaurants: EstablishmentService, chains: ChainService) extends LoggingSupport {

  logger.info("Populating Database")

  private def closed = (WeekTimes.empty, WeekTimes.empty)

  private val subway = Restaurant(chainID = "subway", address = "Raiņa Bulvāris 7", openHours = closed)
  private val pankukas = Restaurant(chainID = "pankukas", address = "9/11 memorial site, NY, USA", openHours = closed)
  private val kfc = Restaurant(chainID = "kfc", address = "Ķekava", openHours = closed)
  private val pelmeni = Restaurant(chainID = "pelmeni", address = "Vecrīgā, Kalķu 7, Rīga", openHours = closed)
  private val mcdonalds = Restaurant(chainID = "mcdonalds", address = "Raiņa Bulvāris 8", openHours = closed)
  private val himalaji = Restaurant(chainID = "himalaji", address = "Blaumaņa iela", openHours = closed)

  /*
  private val public = User(UUID.fromString("00000000-0000-0000-0000-000000000000"), "Public", Some(25576439), Some("pusdieno@krikis.org"), WeekPlan.empty)
  private val dalai = User(id = UUID.fromString("00000000-0000-0000-0000-000000000001"), mobile = Some(42042069), name = "Dalai Lama", eatsAt = WeekPlan.empty)
  private val vaira = User(id = UUID.fromString("00000000-0000-0000-0000-000000000002"), name = "Vaira Vīķe Freiberga", eatsAt = WeekPlan.empty)
  private val tyrion = User(id = UUID.fromString("00000000-0000-0000-0000-000000000003"), name = "Tyrion Lannister", eatsAt = WeekPlan.empty)
  private val martins = User(id = UUID.fromString("00000000-0000-0000-0000-000000000004"), name = "Mārtiņš Rītiņš", eatsAt = WeekPlan.empty)
  private val ziedonis = User(id = UUID.fromString("00000000-0000-0000-0000-000000000005"), name = "Imants Ziedonis", eatsAt = WeekPlan.empty)
  private val twisty = User(id = UUID.fromString("00000000-0000-0000-0000-000000000006"), name = "Twisty the clown", eatsAt = WeekPlan.empty)
  private val steve = User(id = UUID.fromString("00000000-0000-0000-0000-000000000007"), name = "Steve Buscemi", eatsAt = WeekPlan.empty)
  private val margaret = User(id = UUID.fromString("00000000-0000-0000-0000-000000000008"), name = "Margaret Thatcher", eatsAt = WeekPlan.empty)
*/

  private val initialFuture: Future[Unit] = chains.clearAll()
    .flatMap(_ => chains.add(Chain(id = "subway")))
    .flatMap(_ => chains.add(Chain(id = "pankukas")))
    .flatMap(_ => chains.add(Chain(id = "kfc")))
    .flatMap(_ => chains.add(Chain(id = "pelmeni")))
    .flatMap(_ => chains.add(Chain(id = "mcdonalds")))
    .flatMap(_ => chains.add(Chain(id = "himalaji")))

  Await.result(initialFuture
    .flatMap(_ => restaurants.add(subway))
    .flatMap(_ => restaurants.add(pankukas))
    .flatMap(_ => restaurants.add(kfc))
    .flatMap(_ => restaurants.add(pelmeni))
    .flatMap(_ => restaurants.add(mcdonalds))
    .flatMap(_ => restaurants.add(himalaji))
    , Duration.Inf
  )
  
}
