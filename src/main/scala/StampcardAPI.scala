package code

import net.liftweb.http.rest.RestHelper
import net.liftweb.json.JsonAST.{JArray, JField, JObject}
import net.liftweb.json.{JValue, Extraction}
import net.liftweb.mapper.By
import net.liftweb.util.Helpers.AsInt
import stampcard.StampType
import code.model.stampcard.Restaurant

object StampcardAPI extends RestHelper {

  def getAllStamps() = {
    Extraction.decompose(StampType.stampTypes)
  }

  def createUser(jsonData : JValue) = {
    null
  }

  def getUser(id:Int) = null

  def deleteUser(id:Int) = null

  def getAllPromotions() = null

  def createPromotion(jsonData:JValue) = null

  def getPromotions(id:Int) = null

  def getAllRestaurants() = {
    val restaurants: List[Restaurant] = Restaurant.findAll
    JArray(restaurants.map(Restaurant.toJson(_)))
  }

  def getRestaurant(id:Int) = {
    val restaurant = Restaurant.findAll(By(Restaurant.id, id))
    // TODO: check if restaurant valid
    restaurant(0): JValue
  }

  def createRestaurant(jsonData:JValue) = {
    val newRestaurant : Restaurant = Restaurant.create
    newRestaurant.name((jsonData \ "name").extract[String])
    newRestaurant.save
    newRestaurant: JValue
  }

  def getAllEligible(userId:Int) = null

  def getAllEligible(userId:Int, n:Int) = null

  def isEligible(userId:Int, promotionId:Int) = null

  def stampCard(jsonData:JValue) = null

  serve {
    // stamp routes
    case "api" :: "stamps" :: Nil JsonGet req => getAllStamps()
    case "api" :: "stamp" :: Nil JsonPost((jsonData, req)) => stampCard(jsonData)
    // card routes
    // user routes
    case "api" :: "user" :: Nil JsonPost((jsonData, req)) => createUser(jsonData)
    case "api" :: "user" :: AsInt(id) :: Nil JsonGet req => getUser(id)
    case "api" :: "user" :: AsInt(id) :: Nil JsonDelete req => deleteUser(id)
    case "api" :: "user" :: AsInt(id) :: "all_eligible" :: Nil JsonGet req => getAllEligible(id)
    case "api" :: "user" :: AsInt(id) :: "all_eligible" :: AsInt(n) :: Nil JsonGet req => getAllEligible(id, n)
    case "api" :: "user" :: AsInt(userId) :: "eligible" :: AsInt(promotionId) :: Nil JsonGet req => isEligible(userId, promotionId)
    // restaurant routes
    case "api" :: "restaurants" :: Nil JsonGet req => getAllRestaurants()
    case "api" :: "restaurant" :: AsInt(id) :: Nil JsonGet req => getRestaurant(id)
    case "api" :: "restaurant" :: Nil JsonPost((jsonData, req)) => createRestaurant(jsonData)
    // promotion routes
    case "api" :: "promotions" :: Nil JsonGet req => getAllPromotions()
    case "api" :: "promotion" :: Nil JsonPost((jsonData, req)) => createPromotion(jsonData)
    case "api" :: "promotion" :: AsInt(id) :: Nil JsonGet req => getPromotions(id)

  }
}