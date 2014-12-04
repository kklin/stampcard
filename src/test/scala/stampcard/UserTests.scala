package stampcard

import java.util.{Calendar, GregorianCalendar}

import org.scalatest.FunSuite

import stampcard._
import stampcard.StampType

/**
 * Created by kklin on 11/13/14.
 */
class UserTests extends FunSuite {
  // dates
  val cal = new GregorianCalendar() // TODO: this is so un-scala, look for a scala date library?
  cal.setTime(cal.getTime()) // set to right now
  cal.add(Calendar.DAY_OF_YEAR, -1)
  val yesterday = cal.getTime()
  cal.add(Calendar.DAY_OF_YEAR, 7)
  val nextWeek = cal.getTime()

  // stamp/card requirements
  val anyStamp = StampRequirement.byType(StampType.Basic)
                .or(StampRequirement.byType(StampType.Universal))

  test("filtering by eligible promotions") {
    val promotion1 = Promotion(cardRequirement = CardRequirement.byNumberOfStamps(1, anyStamp),
      restaurant = null,
      description = "",
      startDate = yesterday,
      endDate = nextWeek)

    val promotion2 = Promotion(cardRequirement = CardRequirement.byNumberOfStamps(3, anyStamp),
      restaurant = null,
      description = "",
      startDate = yesterday,
      endDate = nextWeek)

    val promotions = List(promotion1, promotion2)

    var customer = Customer(login = "kklin", name = "Kevin Lin")
    val cardWithTwoStamps = Card(stamps = List(Stamp(stampType = StampType.Basic, restaurant = null),
                                               Stamp(stampType = StampType.Basic, restaurant = null)))
    customer = customer.updateCard(cardWithTwoStamps)

    var eligiblePromotions = customer eligiblePromotions promotions

    assert(eligiblePromotions === List(promotion1))

    val cardWithThreeStamps = Card(stamps = List(Stamp(stampType = StampType.Basic, restaurant = null),
                                                Stamp(stampType = StampType.Basic, restaurant = null),
                                                Stamp(stampType = StampType.Basic, restaurant = null)))

    customer = customer.updateCard(cardWithThreeStamps)
    eligiblePromotions = customer eligiblePromotions promotions

    assert(eligiblePromotions === List(promotion1, promotion2))

  }
}