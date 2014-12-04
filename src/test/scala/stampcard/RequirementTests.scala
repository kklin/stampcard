package stampcard

import org.scalatest.FunSuite

import stampcard._

/**
 * Created by kklin on 11/13/14.
 */
class RequirementTests extends FunSuite {
  val aBasicStamp = Stamp(null, StampType.Basic)
  val aUniversalStamp = Stamp(null, StampType.Universal)
  val kklin = Customer("kklin", "Kevin Lin")
  val spatel7 = Customer("spatel7", "Sahil Patel")
  val basicStampKklin = Stamp(restaurant = null, stampType = StampType.Basic, owner = kklin)
  val basicStampSpatel7 = Stamp(restaurant = null, stampType = StampType.Basic, owner = spatel7)

  test("stamp can't be both universal and basic") {
    val stampRequirement = StampRequirement.byType(StampType.Basic)
                           .and(StampRequirement.byType(StampType.Universal))
    assert(stampRequirement.eval(aBasicStamp) === false)
    assert(stampRequirement.eval(aUniversalStamp) === false)

  }

  test("stamp is either universal or basic") {
    val stampRequirement = StampRequirement.byType(StampType.Basic)
                           .or(StampRequirement.byType(StampType.Universal))
    assert(stampRequirement.eval(aBasicStamp) === true)
    assert(stampRequirement.eval(aUniversalStamp) === true)

  }

  test("can only be used if requirement fits from multiple owners") {
    val stampRequirement = StampRequirement.byType(StampType.Basic)
    val cardRequirement = CardRequirement.byNumberOfStampOwners(2, stampRequirement);
    val cardFail = Card(List(basicStampKklin, basicStampKklin)) // two stamps of same owner
    val cardPass = Card(List(basicStampKklin, basicStampSpatel7)) // two stamps of different owner
    assert(cardRequirement.eval(cardFail) === false)
    assert(cardRequirement.eval(cardPass) === true)
  }

  test("can only be used if there's at least three stamps, coming from at least two owners") {
    val stampRequirement = StampRequirement.byType(StampType.Basic)
    val cardRequirement1 = CardRequirement.byNumberOfStamps(3, stampRequirement)
    val cardRequirement2 = CardRequirement.byNumberOfStampOwners(2, stampRequirement)
    val finalCardRequirement = cardRequirement1.and(cardRequirement2)
    val cardFail = Card(List(basicStampKklin, basicStampKklin, basicStampKklin) )
    val cardPass = Card(List(basicStampKklin, basicStampKklin, basicStampSpatel7) )
    assert(finalCardRequirement.eval(cardFail) === false)
    assert(finalCardRequirement.eval(cardPass) === true)
  }
}
