package net.surguy.xom

import org.specs.SpecificationWithJUnit
import net.surguy.xom.XpathImplicits._
import java.io.StringReader
import nu.xom.{Document, Builder}

/**
 * Test using XOM with Saxon XPath via implicits.
 *
 * @author Inigo Surguy
 * @created 19/02/2011 18:20
 */
class XPathSpec extends SpecificationWithJUnit {

  "querying a default-namespace XML document" should {
    val xml = toXom("<root>Some<b>XML</b> with <b>multiple bold</b> bits!</root>")
    "find multiple results for an XPath 1 query" in {
        xml.selectNodes("//b").size must beEqualTo(2)
    }
    "find multiple results for an XPath 2 query" in {
      xml.selectNodes("//b[matches(.,'X.*')]").size must beEqualTo(1)
    }
    "return an empty list when there are no results" in {
      xml.selectNodes("//no_such_element").size must beEqualTo(0)
    }
  }

  "calling selectSingleNode" should {
    val xml = toXom("<root>Some<b>XML</b> with <b>multiple bold</b> bits!</root>")
    "return a populated Option when the selection exists" in {
      xml.selectSingleNode("//b[matches(.,'X.*')]") must beSomething
    }
    "return None when there are no results" in {
      xml.selectSingleNode("//no_such_element") must beNone
    }
    "return the first result when there are multiple results" in {
      xml.selectSingleNode("//b") must beSomething
    }
  }

  "querying a document with namespaces" should {
    val xml = toXom("<root xmlns=\"urn:something\" xmlns:a=\"urn:ns_a\">Some<b>XML</b> with <b>multiple bold</b> <a:em>bits!</a:em></root>")
    "find results in the unprefixed namespace when the namespace context maps it to a prefix" in {
      xml.selectNodes("//d:b")(Map("d" -> "urn:something")).size must beEqualTo(2)
    }
    "find results when the namespace prefix matches one defined in the document" in {
      xml.selectNodes("//a:em")(Map("a" -> "urn:ns_a")).size must beEqualTo(1)
    }
    "find nothing when an unbound namespace prefix is specified" in {
      xml.selectNodes("//z:em")(Map("a" -> "urn:ns_a")).size must beEqualTo(0)
    }
    "use an implicit namespace context if available" in {
      implicit val context = Map("a" -> "urn:ns_a")
      xml.selectNodes("//a:em").size must beEqualTo(1)
    }
  }

  private def toXom(xml: String): Document = new Builder().build(new StringReader(xml))

}