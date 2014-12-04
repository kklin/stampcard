package stampcard

import java.util.Date

/**
 * Created by kklin on 11/12/14.
 */

// val DefaultPromotionPeriod =
// TODO: ensure startDate < endDate ?
case class Promotion(cardRequirement:CardRequirement, restaurant: Restaurant, description:String, startDate:Date = new Date(), endDate:Date) {

  def isEligible(card:Card) = cardRequirement.eval(card) && isEligibleDate(new Date())

  def isEligibleDate(testDate:Date):Boolean = testDate.before(endDate) && testDate.after(startDate)

  def extendPromotionTo(extendTo:Date) =  Promotion(cardRequirement, restaurant, description, startDate, extendTo)

}

object Promotion {
  var allPromotions:List[Promotion] = List[Promotion]()
}
