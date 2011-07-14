package bootstrap.liftweb

import net.liftweb._
import net.liftweb.util._
import net.liftweb.util.Helpers._
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.sitemap._
import net.liftweb.sitemap.Loc._

import code.snippet._

class Boot {
    def siteMapEntries =    (Menu("Home") / "index") ::
                            (Menu("Items") / "items") ::
                            (Menu("Items") / "items" / "show" >> Hidden) ::
                            Nil

    def siteMap = SiteMap(siteMapEntries:_*)

    def boot {
        // where to search snippet
        LiftRules.addToPackages("code")

        LiftRules.dispatch.append(ItemsController)

        LiftRules.setSiteMap(siteMap)
    }
}
