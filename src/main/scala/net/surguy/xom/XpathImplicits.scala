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
      val nodes = runQuery(query)
      nodes.foldLeft(new Nodes())((xomNodes, node) => { xomNodes.append(node); xomNodes } )
    }

    def selectSingleNode(query: String): Option[Node] = runQuery(query).headOption

    private def runQuery(query: String): java.util.List[Node] = {
      val factory = new XPathFactoryImpl()
      val doc = new DocumentWrapper(node, null, factory.getConfiguration)
      val expr = factory.newXPath().compile(query)

      expr.evaluate(doc, XPathConstants.NODESET).asInstanceOf[java.util.List[Node]]
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