package net.surguy.xom

import net.sf.saxon.om.NamespaceConstant
import net.sf.saxon.xom.DocumentWrapper
import net.sf.saxon.xpath.XPathFactoryImpl
import nu.xom.{Nodes, Node}
import javax.xml.xpath.{XPathConstants, XPathFactory}
import nu.xom.converters.DOMConverter
import org.w3c.dom
import dom._
import scala.collection.JavaConversions._

/**
 * Make XPath2 available in XOM, via Saxon.
 *
 * @author Inigo Surguy
 * @created 19/02/2011 18:11
 */

object XpathImplicits {

  implicit def PimpWithGetDocument(node: Node) = new PimpedNode(node)

  class PimpedNode(node: Node) {
    def selectNodes(query: String): Nodes = {
      val factory = new XPathFactoryImpl()
      val doc = new DocumentWrapper(node, null, factory.getConfiguration)
      val xpath = factory.newXPath()
      val expr = xpath.compile(query)

      val result = expr.evaluate(doc, XPathConstants.NODESET)
      val nodes : java.util.List[Node] = result.asInstanceOf[java.util.List[Node]]

      val xomNodes: Nodes = new Nodes()
      for (i <- (0 until nodes.size)) {
        xomNodes.append( nodes.get(i).asInstanceOf[Node] )
      }
      xomNodes
    }

    def selectSingleNode(xpath: String): Option[Node] = {
      None
    }
  }

  private def domToXom(node: dom.Node): nu.xom.Node = node match {
    case n: Element => DOMConverter.convert(n)
    case n: Text => DOMConverter.convert(n)
    case n: ProcessingInstruction => DOMConverter.convert(n)
    case n: Attr => DOMConverter.convert(n)
    case n: Comment => DOMConverter.convert(n)
    case n: Document => DOMConverter.convert(n)
    case n: DocumentType => DOMConverter.convert(n)
  }

}