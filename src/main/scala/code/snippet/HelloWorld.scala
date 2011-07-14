package code.snippet

import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.java.util.Date
import Helpers._

class HelloWorld {
    // replace the contents of the element with id "time" with the date
    def howdy = "#time *" #> (new Date).toString
}
