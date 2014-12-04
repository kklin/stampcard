package code.model.stampcard

import net.liftweb.json.Extraction
import net.liftweb.json.JsonAST.{JObject, JString, JField, JValue}
import net.liftweb.mapper._

/**
 * Created by kklin on 11/12/14.
 */
//case class Restaurant(name:String, description:String, promotions:List[Promotion], authorized_stampers:List[Stamper]) {
//  def addPromotion(promotion:Promotion) = Restaurant(name, description, promotion :: promotions, authorized_stampers)
//
//  def endPromotion(promotion:Promotion) = null
//
//  def addAuthorizedStamper(stamper:Stamper) = null
//}

class Restaurant extends LongKeyedMapper[Restaurant] {
  def getSingleton = Restaurant

  def primaryKeyField = id
  object id extends MappedLongIndex(this)
  object name extends MappedString(this, 50)
}

object Restaurant extends Restaurant with LongKeyedMetaMapper[Restaurant] {
    implicit def toJson(restaurant : Restaurant) = JObject(JField("id", JString(restaurant.id.toString())) ::
      JField("name", JString(restaurant.name.toString())) :: Nil)
}
