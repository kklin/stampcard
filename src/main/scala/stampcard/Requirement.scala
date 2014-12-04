package stampcard

import stampcard.StampType

/**
 * Created by kklin on 11/14/14.
 */

// TODO: we should be able to use traits and refactor this to be more DRY
case class StampRequirement(requirement:(Stamp => Boolean)) {

  def and(newRequirement:StampRequirement):StampRequirement = {
    def mergedRequirements(stamp:Stamp):Boolean = newRequirement.requirement(stamp) && requirement(stamp)
    StampRequirement(mergedRequirements)
  }

  def or(newRequirement:StampRequirement):StampRequirement = {
    def mergedRequirements(stamp:Stamp):Boolean = newRequirement.requirement(stamp) || requirement(stamp)
    StampRequirement(mergedRequirements)
  }

  def eval(stamp:Stamp): Boolean = {
    requirement(stamp)
  }

}

object StampRequirement {
  def byRestaurant(restaurant:Restaurant) = StampRequirement((stamp:Stamp) => (stamp.restaurant == restaurant))

  def byType(stampType:StampType) = StampRequirement((stamp:Stamp) => (stamp.stampType == stampType))
}

case class CardRequirement(requirement:(Card => Boolean)) {
  def and(newRequirement:CardRequirement):CardRequirement = {
    def mergedRequirements(card:Card):Boolean = newRequirement.requirement(card) && requirement(card)
    CardRequirement(mergedRequirements)
  }

  def or(newRequirement:CardRequirement):CardRequirement = {
    def mergedRequirements(card:Card):Boolean = newRequirement.requirement(card) || requirement(card)
    CardRequirement(mergedRequirements)
  }

  def eval(card:Card): Boolean = {
    requirement(card)
  }

}

object CardRequirement {
  def byNumberOfStamps(minStamps:Int, stampRequirement:StampRequirement) =
    CardRequirement(
      (card:Card) => (card.stamps filter stampRequirement.requirement).size >= minStamps
    )

  def byNumberOfStampOwners(minOwners:Int, stampRequirement:StampRequirement) =
    CardRequirement(
      (card:Card) => ((card.stamps filter stampRequirement.requirement) map (stamp => stamp.owner)).distinct.size >= minOwners
    )
}
