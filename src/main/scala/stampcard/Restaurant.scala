package stampcard

/**
 * Created by kklin on 11/12/14.
 */
case class Restaurant(name:String, description:String, promotions:List[Promotion], authorized_stampers:List[Stamper]) {
  def addPromotion(promotion:Promotion) = Restaurant(name, description, promotion :: promotions, authorized_stampers)

  def endPromotion(promotion:Promotion) = null

  def addAuthorizedStamper(stamper:Stamper) = null

}
