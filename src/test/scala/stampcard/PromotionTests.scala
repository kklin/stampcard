package stampcard

import org.scalatest.FunSuite

import stampcard._
import java.util.Date
import java.util.{Calendar, GregorianCalendar}

/**
 * Created by kklin on 11/13/14.
 */
class PromotionTests extends FunSuite {

  val cal = new GregorianCalendar() // TODO: this is so un-scala, look for a scala date library?
  cal.setTime(cal.getTime()) // set to right now
  cal.add(Calendar.DAY_OF_YEAR, -1)
  val yesterday = cal.getTime()
  cal.add(Calendar.DAY_OF_YEAR, 7)
  val nextWeek = cal.getTime()

  val alwaysTrueRequirement = CardRequirement((card:Card) => true)
  val alwaysFalseRequirement = CardRequirement((card:Card) => false)

  val anyStamp = StampRequirement.byType(StampType.Basic)
                 .or(StampRequirement.byType(StampType.Universal))
  val atLeastOneStamp = CardRequirement.byNumberOfStamps(1, anyStamp)

  test("promotion not rejecting when meetsStampRequirement returns false") {
    val promotion = Promotion(cardRequirement = alwaysFalseRequirement,
      restaurant = null,
      description = "",
      startDate = yesterday,
      endDate = nextWeek)
    assert(promotion.isEligible(null) === false)
  }

  test("promotion date range rejecting appropriate card") {
    val promotion = Promotion(cardRequirement = alwaysTrueRequirement,
                              restaurant = null,
                              description = "",
                              startDate = yesterday,
                              endDate = nextWeek)
    assert(promotion.isEligible(null) === true)
  }

  test("promotion date range accepting inappropriate card") {
    val promotion = Promotion(cardRequirement = alwaysTrueRequirement,
      restaurant = null,
      description = "",
      startDate = nextWeek,
      endDate = nextWeek)
    assert(promotion.isEligible(null) === false)
  }

  test("simple meetsStampRequirement of at least one stamp of any kind") {
    val card = Card(stamps = List(Stamp(null, StampType.Basic, null)))
    val promotion = Promotion(cardRequirement = atLeastOneStamp,
      restaurant = null,
      description = "",
      startDate = yesterday,
      endDate = nextWeek)
    assert(promotion.isEligible(card) === true)
  }

  test("simple meetsStampRequirement of at least one stamp of any kind on blank card") {
    val card = Card()
    def requirement(card:Card):Boolean = card.stamps.size >= 1
    val promotion = Promotion(cardRequirement = atLeastOneStamp,
      restaurant = null,
      description = "",
      startDate = yesterday,
      endDate = nextWeek)
    assert(promotion.isEligible(card) === false)
  }
}
