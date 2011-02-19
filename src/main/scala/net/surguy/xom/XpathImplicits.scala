package net.surguy.xom

import net.sf.saxon.xom.DocumentWrapper
import net.sf.saxon.xpath.XPathFactoryImpl
import nu.xom.{Nodes, Node}
import javax.xml.xpath.XPathConstants
import org.w3c.dom
import dom._
import scala.collection.JavaConversions._
import net.sf.saxon.pull.NamespaceContextImpl
import net.sf.saxon.om.InscopeNamespaceResolver

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
      val context = new NamespaceContextImpl(new InscopeNamespaceResolver(doc.getRoot))
      val xpath = factory.newXPath()
      xpath.setNamespaceContext(context)
      val expr = xpath.compile(query)

      expr.evaluate(doc, XPathConstants.NODESET).asInstanceOf[java.util.List[Node]]
    }
  }

}