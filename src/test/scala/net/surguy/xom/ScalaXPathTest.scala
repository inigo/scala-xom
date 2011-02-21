package net.surguy.xom

import org.specs.SpecificationWithJUnit
import Implicits._

/**
 * Test applying XPath to Scala XML via XOM.
 *
 * @author Inigo Surguy
 * @created 20/02/2011 19:18
 */
class ScalaXPathTest extends SpecificationWithJUnit {

  "applying XPath" should {
    "return results when applied to converted Scala XML" in {
      val node = <root>Here is <b>some</b> XML</root>.toXom.selectSingleNode("//b")
      node must beSomething
    }
  }

}
