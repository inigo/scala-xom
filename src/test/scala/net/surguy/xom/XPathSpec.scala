package net.surguy.xom

import org.specs.SpecificationWithJUnit
import net.surguy.xom.XpathImplicits._
import java.io.StringReader
import nu.xom.Builder

/**
 * Test for XPath
 *
 * @author Inigo Surguy
 * @created 19/02/2011 18:20
 */
class XPathSpec extends SpecificationWithJUnit {

  "querying a default-namespace XML document" should {
    val xml = new Builder().build(new StringReader("<root>Some<b>XML</b> with <b>multiple bold</b> bits!</root>"))
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

  /*
  "calling selectSingleNode" should {
    "return a populated Option when the selection exists" in {

    }
    "return None when there are no results" in {

    }
    "return None when there are multiple results" in {

    }
  }

  "querying a document with namespaces" should {
    "find results when the namespace is specified" in {

    }
    "find nothing when no namespace is specfied" in {

    }
    "find nothing when an incorrect namespace is specified" in {

    }
  }
  */

}