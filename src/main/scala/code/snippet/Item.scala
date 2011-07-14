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

object Crud {
    trait Base extends MVCHelper {
        type Entity

        implicit def CssSel2Option(sel: CssSel) = Some(sel)

        val Prefix: String

        def linkTo(label: String, param: String) =
            <a href={"/" + Prefix + "/" + param}>{label}</a>

        protected def render(opt: Option[CssSel], tplName: String) =
            Templates(Prefix :: tplName :: Nil).flatMap(t => opt.map(_(t)))
    }

    trait Index extends Base {
        def index: Option[CssSel]

        serve {
            case Prefix :: Nil => render(index, "index")
        }
    }

    trait Show extends Base {
        object entity extends RequestVar[Option[Entity]](None)

        def find(param: String): Option[Entity]
        def show(obj: Entity): Option[CssSel]

        serve {
            case Prefix :: param :: Nil => find(param) map { e =>
                entity(Some(e))
                render(show(e), "show")
            }
        }

    }

    trait All extends Index with Show
}

object ItemsController extends Crud.All {
    type Entity = Item
    val Prefix = "items"

    def find(name: String) = ItemStorage.get(name)

    def index = ".row *" #> ItemStorage.all.map { item =>
        ".name *"  #> item.name &
        ".show *"  #> linkTo("Show", item.name)
    }

    def show(item: Item) = ".name" #> item.name
}

object AddItem extends LiftScreen {
    val name = field("Name", "", trim, valMinLen(1, "Name can not be blank"))

    def finish() {
        ItemStorage.save(Item(name.is))
        S.redirectTo("/items/" + name.is, () => S.notice("Item " + name.is + " created"))
    }
}

class EditItem extends LiftScreen {
    val name = field("Name", ItemsController.entity.get.map(_.name) getOrElse "", trim, valMinLen(1, "Name can not be blank"))

    def finish() {
        ItemStorage.save(Item(name.is))
        S.redirectTo("/items/" + name.is, () => S.notice("Item " + name.is + " created"))
    }
}

