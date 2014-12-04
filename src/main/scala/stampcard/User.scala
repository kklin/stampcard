package stampcard

import stampcard.StampType

/**
 * Created by kklin on 11/12/14.
 */

trait User {
  val login: String
  val name: String
}

case class Customer(login: String, name: String, card: Card = Card()) extends User {

  def eligiblePromotions(promotions:List[Promotion] = Promotion.allPromotions) = {
    for {
      promotion <- promotions if promotion isEligible card
    } yield promotion
  }

  def updateCard(newCard:Card) = Customer(login, name, newCard)
}

case class Stamper(login: String, name: String, restaurant: Restaurant) extends User {
  def stampCard(card: Card, stampType: StampType) = card.stamp(stampType, restaurant, this)

}

object AnonOwner extends Customer(name = "ANON OWNER", login = "ANON OWNER") {
}

object AnonStamper extends Stamper(name = "ANON STAMPER", login = "ANON STAMPER", restaurant = null) { // TODO: make a obejct for undefined restaurant?
}

object Customer {
}
