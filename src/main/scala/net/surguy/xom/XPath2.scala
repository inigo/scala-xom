package net.surguy.xom

/**
 * @todo Add some documentation!
 *
 * @author Inigo Surguy
 * @created 17/02/2011 22:16
 */

import net.sf.saxon.sxpath._
import net.sf.saxon.xom._
import java.io.StringReader
import net.sf.saxon.Configuration
import net.sf.saxon.om.NamespaceConstant
import nu.xom.{Nodes, Builder}
import org.w3c.dom.{Node, NodeList}
import javax.xml.xpath.{XPathConstants, XPathFactory}
import net.sf.saxon.xpath.XPathFactoryImpl

object XPath2 extends Application {
  override def main(args: Array[String]) = {
    // net.sf.saxon.xpath - the JAXP API
    // net.sf.saxon.sxpath, Saxon's own API, which is better adapted to XPath 2.0

    // http://saxon.sourceforge.net/saxon7.5/api-guide.html

    System.setProperty("javax.xml.xpath.XPathFactory:" + NamespaceConstant.OBJECT_MODEL_SAXON, "net.sf.saxon.xpath.XPathFactoryImpl");

    val xml = new Builder().build(new StringReader("<root>Some<b>XML</b>!</root>"))


    val factory = XPathFactory.newInstance(NamespaceConstant.OBJECT_MODEL_SAXON);
    val configuration = factory.asInstanceOf[XPathFactoryImpl].getConfiguration

    val doc = new DocumentWrapper(xml, null, configuration)

    val xpath = factory.newXPath();
    val expr = xpath.compile("//b[matches(.,'X.*')]");

    val result = expr.evaluate(doc, XPathConstants.NODESET)
    println(result)

//    val nodes : NodeList = result.asInstanceOf[NodeList];
//    val sharped = new Nodes(nodes);

//
//    for (val n <- sharped) {
//      println(n.toString());
//    }
  }
}