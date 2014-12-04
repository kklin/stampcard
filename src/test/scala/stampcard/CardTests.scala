package stampcard

import org.scalatest.FunSuite
import stampcard.StampType

import stampcard._

/**
 * Created by kklin on 11/13/14.
 */
class CardTests extends FunSuite {

  test("card should not have any stamps yet") {
    val card = Card()
    assert(card.stamps.size === 0)
  }

  test("card should now be stamped") {
    var card = Card()
    card = card.stamp(restaurant = null)
    assert(card.stamps === List(Stamp(stampType = StampType.Basic, restaurant = null)))
  }
}
