package net.surguy.xom

import org.specs.SpecificationWithJUnit
import Implicits._
import nu.xom.{Attribute => XomAttribute, Element => XomElement, Node => XomNode, Text => XomText, Comment => XomComment,
              ProcessingInstruction => XomProcessingInstruction, Document => XomDocument, Nodes => XomNodes}

/**
 * Test the implicits that pimp XOM nodes.
 *
 * @author Inigo Surguy
 * @created 20/02/2011 17:16
 */
class ImplicitsSpec extends SpecificationWithJUnit {

  "using implicits for XOM nodes" should {
    val xml: XomElement = (<root x="1" y="2"><a/><b><c/> text </b>some text</root>).toXom.asInstanceOf[XomElement]
    "allow looping through child elements" in {
      xml.elements.size must beEqualTo(2)
      println(xml.elements.toString)
    }
    "allow looping through attributes" in {
      xml.attributes.size must beEqualTo(2)
      println(xml.attributes.toString)
    }
    "allow looping through children" in {
      xml.nodes.size must beEqualTo(3)
    }
  }

}