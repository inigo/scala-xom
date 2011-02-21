package net.surguy.xom

import org.specs.SpecificationWithJUnit
import Implicits._
import nu.xom.{Builder, Attribute => XomAttribute, Element => XomElement, Node => XomNode, Text => XomText, Comment => XomComment, ProcessingInstruction => XomProcessingInstruction, Document => XomDocument, Nodes => XomNodes}
import xml.Elem
import java.io.StringReader

/**
 * Test the implicits that pimp XOM nodes.
 *
 * @author Inigo Surguy
 * @created 20/02/2011 17:16
 */
class ImplicitsTest extends SpecificationWithJUnit {

  "using implicits for XOM nodes" should {
    val xml: XomElement = (<root x="1" y="2"><a/><b><c/> text </b>some text</root>).toXom.getRootElement
    "allow looping through child elements" in {
      xml.elements.size must beEqualTo(2)
    }
    "allow looping through attributes" in {
      xml.attributes.size must beEqualTo(2)
      println(xml.attributes.toString)
    }
    "allow looping through children" in {
      xml.nodes.size must beEqualTo(3)
    }
  }

  "Converting specific classes to and from XOM" should {
    "return XomElement for Elem" in {
      val xml: XomDocument = <root/>.toXom
      xml must haveClass[XomDocument]
    }
    "return Elem for XomElement" in {
      val xml: Elem = new Builder().build(new StringReader("<root/>")).getRootElement.toScalaXml
      xml must haveClass[Elem]
    }
  }

}