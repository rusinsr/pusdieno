package services.daos

import java.util.UUID

import models.{Contact, User}

import scala.concurrent.Future

trait ContactService {

  def contactsOfUser(userID: UUID): Future[Seq[Contact]]

  def friendsWithContactInfo(userID: UUID): Future[Seq[(Contact, User)]]

  def contactsWithOptionalDBUserInfo(userID: UUID): Future[Seq[(Contact, Option[DBUser])]] =
    db.run((contacts.filter(_.ownerID === userID) joinLeft users on (_.contactID === _.id)).result)

  def friendsWithStatusInfo(userID: UUID): Future[Seq[(Contact, User, Boolean, Boolean)]] =
    db.run(
      (for {
        (contact, userInfo) <- friendsWithContactInfoQuery(userID)
      } yield (contact, userInfo, ChoiceDAO.wantsFood(userInfo._1.id), ChoiceDAO.wantsCoffee(userInfo._1.id))).result
    ).map(_.map {
      case (contact, userInfo, wantsFood, wantsCoffee) => (contact, (User.fromDB _).tupled(userInfo), wantsFood, wantsCoffee)
    })

  def belongsTo(contactID: UUID): Future[Option[UUID]] =
    db.run(contacts.filter(c => c.id === contactID).map(_.ownerID).result.headOption)

  def get(contactID: UUID): Future[Option[Contact]] = db.run(contacts.filter(_.id === contactID).result.headOption)

  //def save(contact: Contact): Future[Int] = db.run(contacts += contact)

  def save(contact: Contact): Future[Int] = {

    db.run(contacts.insertOrUpdate(contact)).flatMap { affectedRows =>
      println("Changed " + affectedRows + "rows in contacts table! " + contact.toString)
      contact.phone match {
        case None => Future.successful(None)
        case Some(phone) => db.run(users.filter(_.phone === phone).map(_.id).result.headOption)
      }
    } flatMap { idO =>
      contact.email match {
        case None => Future(idO)
        case Some(email) => db.run(users.filter(_.email === email).map(_.id).result.headOption)
      }
    } flatMap {
      case None => Future.successful(0)
      case Some(id) => db.run(contacts.filter(_.id === contact.id).map(_.contactID).update(Some(id)))
    }
  }

  def delete(contactID: UUID): Future[Int] = db.run(contacts.filter(_.id === contactID).delete)



  def friendsOfUserQuery(userID: Rep[UUID]): Query[ContactTable, Contact, Seq] = for {
    c <- contacts.filter(_.ownerID === userID)
    f <- contacts.filter(friend => friend.ownerID === c.contactID && friend.contactID === userID)
  } yield f

  private def friendsWithContactInfoQuery(userID: UUID) = for {
    contact <- friendsOfUserQuery(userID)
    userInfo <- UserDAO.getFromID(contact.ownerID)
  } yield (contact, userInfo)

  def linkNewUser(dbUser: DBUser): Future[Int] = (dbUser.phone match {
    case None => Future.successful(0)
    case Some(phone) => db.run(contacts.filter(_.contactPhone === phone).map(_.contactID).update(Some(dbUser.id)))
  }).flatMap { _ =>
    dbUser.email match {
      case None => Future.successful(0)
      case Some(email) => db.run(contacts.filter(_.contactEmail === email).map(_.contactID).update(Some(dbUser.id)))
    }
  }
}
