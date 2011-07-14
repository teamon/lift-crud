package code.snippet

import net.liftweb.http._
import net.liftweb.util._
import net.liftweb.util.Helpers._

import scala.xml.NodeSeq

object ItemStorage {
    val data = new scala.collection.mutable.ListBuffer[Item]()
    def all = data.toList
    def get(name: String) = data.find(_.name == name)
    def save(i: Item) { data += i }
    def remove(i: Item) { data -= i }
}

case class Item(name: String)

trait Crud extends MVCHelper {
    implicit def CssSel2Option(sel: CssSel) = Some(sel)

    val Prefix: String

    val Index = Prefix :: Nil

    def index: Option[CssSel]
    def show(param: String): Option[CssSel]

    serve {
        case Prefix :: Nil => render(index, "index")
        case Prefix :: param :: Nil => render(show(param), "show")
    }

    def linkTo(label: String, param: String) =
        <a href={"/" + Prefix + "/" + param}>{label}</a>

    protected def render(opt: Option[CssSel], tplName: String) =
        Templates(Prefix :: tplName :: Nil).flatMap(t => opt.map(_(t)))
}

object ItemsController extends Crud {
    val Prefix = "items"

    def index = ".row *" #> ItemStorage.all.map { item =>
        ".name *"  #> item.name &
        ".show *"  #> linkTo("Show", item.name)
    }

    def show(name: String) = ItemStorage.get(name).map { item =>
        ".name" #> item.name
    }
}

object AddItem extends LiftScreen {
    val name = field("Name", "", trim, valMinLen(1, "Name can not be blank"))

    def finish() {
        ItemStorage.save(Item(name.is))
        S.redirectTo("/items/" + name.is, () => S.notice("Item " + name.is + " created"))
    }
}
