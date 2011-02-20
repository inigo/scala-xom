package net.surguy.xom

import org.specs.SpecificationWithJUnit
import nu.xom.Builder
import nu.xom.{Node => XomNode}
import nu.xom.canonical.Canonicalizer
import java.io.{ByteArrayOutputStream, StringReader}
import scala.xml
import xml.parsing.ConstructingParser
import xml.XML
import io.Source

/**
 * Test converting between the Scala XML form and XOM.
 *
 * @author Inigo Surguy
 * @created 20/02/2011 12:58
 */

class XomConverterSpec  extends SpecificationWithJUnit {

  "converting from Scala XML to XOM" should {
    "produce identical results for a single element containing text" in {
      testConversion("<root>Some elements</root>")
    }
    "produce identical results for multiple nested elements" in {
      testConversion("<root><b>Some</b> <i>things<span>elements</span> and so on<x><y><z/></y></x></i></root>")
    }
    "produce identical results for attributes" in {
      testConversion("<root a=\"1\" b=\"2\">Some <span class=\"this\">things</span></root>")
    }
    "produce identical results for comments" in {
      testConversion("<root>Before comment <!-- Comments <commentedOut/> --> and before empty comment <!-- --></root>")
    }
    "produce identical results for processing instructions" in {
      testConversion("<root>Some <?some thing?> with PIs</root>")
    }
    "produce identical results for namespaced elements in an unprefixed namespace" in {
      testConversion("<root xmlns=\"urn:test\">Some <b>element</b></root>")
    }
    "produce identical results for namespaced elements in prefixed namespaces" in {
      testConversion("<root xmlns:a=\"urn:test\">Some <a:b>element</a:b></root>")
    }
    "produce identical results for multiple namespaced elements in prefixed namespaces" in {
      testConversion("<root xmlns:a=\"urn:test\" xmlns:b=\"urn:test2\">Some <a:x><b:y/>element<c:z xmlns:c=\"urn:test3\"/></a:x></root>")
    }
    "produce identical results for namespaced attributes in prefixed namespaces" in {
      testConversion("<root xmlns:a=\"urn:test\">Some <b a:value=\"one\">element</b></root>")
    }
  }

  "converting from XOM to Scala XML" should {
    "produce identical results for a single element containing text" in {
      testScalaConversion("<root>Some elements</root>")
    }
    "produce identical results for multiple nested elements" in {
      testScalaConversion("<root><b>Some</b> <i>things<span>elements</span> and so on<x><y><z/></y></x></i></root>")
    }
    "produce identical results for attributes" in {
      testScalaConversion("<root a=\"1\" b=\"2\">Some <span class=\"this\">things</span></root>")
    }
    "produce identical results for comments" in {
      testScalaConversion("<root>Before comment <!-- Comments <commentedOut/> --> and before empty comment <!-- --></root>")
    }
    "produce identical results for processing instructions" in {
      testScalaConversion("<root>Some <?some thing?> with PIs</root>")
    }
    "produce identical results for namespaced elements in an unprefixed namespace" in {
      testScalaConversion("<root xmlns=\"urn:test\">Some <b>element</b></root>")
    }
    "produce identical results for namespaced elements in prefixed namespaces" in {
      testScalaConversion("<root xmlns:a=\"urn:test\">Some <a:b>element</a:b></root>")
    }
    "produce identical results for multiple namespaced elements in prefixed namespaces" in {
      testScalaConversion("<root xmlns:a=\"urn:test\" xmlns:b=\"urn:test2\">Some <a:x><b:y/>element<c:z xmlns:c=\"urn:test3\"/></a:x></root>")
    }
    "produce identical results for namespaced attributes in prefixed namespaces" in {
      testScalaConversion("<root xmlns:a=\"urn:test\">Some <b a:value=\"one\">element</b></root>")
    }
  }

  private def testConversion(xml: String) = {
    // We need to use ConstructingParser because XML.loadString doesn't preserve comments
    val scalaXml = ConstructingParser.fromSource(Source.fromString(xml), true).document.docElem
    val xomXml = new Builder().build(new StringReader(xml))
    val convertedToXom = XomConverter.toXom(scalaXml)
    canonical(xomXml.getRootElement) must beEqualTo(canonical(convertedToXom))
  }

  private def testScalaConversion(xml: String) = {
    // We need to use ConstructingParser because XML.loadString doesn't preserve comments
    val scalaXml = ConstructingParser.fromSource(Source.fromString(xml), true).document.docElem
    val xomXml = new Builder().build(new StringReader(xml))
    val convertedToScala = XomConverter.toScalaXml(xomXml)
    println(convertedToScala)
    scalaXml must beEqualTo(convertedToScala)
    canonical(xomXml.getRootElement) must beEqualTo(canonical(XomConverter.toXom(convertedToScala)))
  }

  private def canonical(xml: XomNode): String = {
    val out = new ByteArrayOutputStream()
    // Canonicalizer.EXCLUSIVE_XML_CANONICALIZATION_WITH_COMMENTS fixes up the namespaces appropriately
    val canon = new Canonicalizer(out, Canonicalizer.EXCLUSIVE_XML_CANONICALIZATION_WITH_COMMENTS)
    canon.write(xml)
    out.toString("UTF-8")
  }
}