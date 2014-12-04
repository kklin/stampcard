package stampcard

import stampcard._
import stampcard.StampType

/**
 * Created by kklin on 11/12/14.
 */
case class Stamp(restaurant:Restaurant, stampType:StampType, stamper:Stamper = AnonStamper, owner:Customer = AnonOwner) {

}

case class StampType(name:String, restaurant:Restaurant) {

}

object StampType {
  val Basic = StampType(name = "BASIC", restaurant = null)
  val Universal = StampType(name = "UNIVERSAL", restaurant = null)
  val stampTypes = List(Basic, Universal)
}