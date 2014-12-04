package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import js.jquery.JQueryArtifacts
import sitemap._
import Loc._
import mapper._

import code.model.stampcard.Restaurant
import net.liftmodules.JQueryModule

import net.liftweb.mapper.{DB, DefaultConnectionIdentifier}

import net.liftweb.mapper.{ConnectionIdentifier, ConnectionManager, Schemifier}
import java.sql.{Connection, DriverManager}
import net.liftweb.common.{Box, Empty, Full}
import net.liftweb.util.Props

object myDBVendor extends ConnectionManager {
  private var pool: List[Connection] = Nil
  private var poolSize = 0
  private val maxPoolSize = 4

  private lazy val chooseDriver = Props.mode match {
    case Props.RunModes.Production => "org.apache.derby.jdbc.EmbeddedDriver"
    case _ => "org.h2.Driver"
  }

  private lazy val chooseURL = Props.mode match {
    case Props.RunModes.Production => "jdbc:derby:lift_mapperexample;create=true"
    case _ => "jdbc:h2:mem:lift_mapperexample;DB_CLOSE_DELAY=-1"
  }

  private def createOne: Box[Connection] = try {
    val driverName: String = Props.get("db.driver") openOr chooseDriver
    val dbUrl: String = Props.get("db.url") openOr chooseURL

    Class.forName(driverName)

    val dm = (Props.get("db.user"), Props.get("db.password")) match {
      case (Full(user), Full(pwd)) =>
        DriverManager.getConnection(dbUrl, user, pwd)

      case _ => DriverManager.getConnection(dbUrl)
    }

    Full(dm)
  } catch {
    case e: Exception => e.printStackTrace; Empty
  }

  def newConnection(name: ConnectionIdentifier): Box[Connection] =
    synchronized {
      pool match {
        case Nil if poolSize < maxPoolSize =>
          val ret = createOne
          poolSize = poolSize + 1
          ret.foreach(c => pool = c :: pool)
          ret

        case Nil => wait(1000L); newConnection(name)
        case x :: xs => try {
          x.setAutoCommit(false)
          Full(x)
        } catch {
          case e => try {
            pool = xs
            poolSize = poolSize - 1
            x.close
            newConnection(name)
          } catch {
            case e => newConnection(name)
          }
        }
      }
    }

  def releaseConnection(conn: Connection): Unit = synchronized {
    pool = conn :: pool
    notify
  }

}
/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
//  def boot {
//    if (!DB.jndiJdbcConnAvailable_?) {
//      val vendor =
//	new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
//			     Props.get("db.url") openOr
//			     "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
//			     Props.get("db.user"), Props.get("db.password"))
//
//      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)
//
//      DB.defineConnectionManager(util.DefaultConnectionIdentifier, vendor)
//    }
//
//    // Use Lift's Mapper ORM to populate the database
//    // you don't need to use Mapper to use Lift... use
//    // any ORM you want
//    Schemifier.schemify(true, Schemifier.infoF _, User)
//
//    // where to search snippet
//    LiftRules.addToPackages("code")
//
//    // Build SiteMap
//    def sitemap = SiteMap(
//      Menu.i("Home") / "index" >> User.AddUserMenusAfter, // the simple way to declare a menu
//
//      // more complex because this menu allows anything in the
//      // /static path to be visible
//      Menu(Loc("Static", Link(List("static"), true, "/static/index"),
//	       "Static Content")))
//
//    def sitemapMutators = User.sitemapMutator
//
//    // set the sitemap.  Note if you don't want access control for
//    // each page, just comment this line out.
//    LiftRules.setSiteMapFunc(() => sitemapMutators(sitemap))
//
//    //Init the jQuery module, see http://liftweb.net/jquery for more information.
//    LiftRules.jsArtifacts = JQueryArtifacts
//    JQueryModule.InitParam.JQuery=JQueryModule.JQuery191
//    JQueryModule.init()
//
//    //Show the spinny image when an Ajax call starts
//    LiftRules.ajaxStart =
//      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
//
//    // Make the spinny image go away when it ends
//    LiftRules.ajaxEnd =
//      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)
//
//    // Force the request to be UTF-8
//    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))
//
//    // What is the function to test if a user is logged in?
//    LiftRules.loggedInTest = Full(() => User.loggedIn_?)
//
//    // Use HTML5 for rendering
//    LiftRules.htmlProperties.default.set((r: Req) =>
//      new Html5Properties(r.userAgent))
//
//    // Make a transaction span the whole HTTP request
//    S.addAround(DB.buildLoanWrapper)
//  }

  def boot: Unit = {
    DB.defineConnectionManager(DefaultConnectionIdentifier, myDBVendor)
    Schemifier.schemify(true, Schemifier.infoF _, Restaurant)

    LiftRules.dispatch.append(code.StampcardAPI)
  }
}
