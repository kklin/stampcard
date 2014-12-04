package stampcard

import stampcard.StampType

/**
 * Created by kklin on 11/12/14.
 */
case class Card(stamps:List[Stamp] = List[Stamp](), owner:User = AnonOwner) {

  def stamp(stampType:StampType = StampType.Basic, restaurant:Restaurant, stamper:Stamper = AnonStamper) = {
    val newStamp = Stamp(restaurant, stampType, stamper)
    Card(newStamp :: stamps, owner)
  }

  def usePromotion(promotion:Promotion) = { // TODO

  }
}

object Card {
}
