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
import javax.xml.namespace.NamespaceContext
import java.lang.String

/**
 * Make XPath2 available in XOM, via Saxon.
 *
 * @author Inigo Surguy
 * @created 19/02/2011 18:11
 */

object XpathImplicits {

  implicit def PimpWithGetDocument(node: Node) = new PimpedNode(node)

  class PimpedNode(node: Node) {
    def selectNodes(query: String)(implicit context:Map[String, String] = Map()): Nodes = {
      val nodes = runQuery(query, context)
      nodes.foldLeft(new Nodes())((xomNodes, node) => { xomNodes.append(node); xomNodes } )
    }

    def selectSingleNode(query: String)(implicit context:Map[String, String] = Map()): Option[Node] = runQuery(query, context).headOption

    private def runQuery(query: String, context: Map[String, String]): java.util.List[Node] = {
      val factory = new XPathFactoryImpl()
      val doc = new DocumentWrapper(node, null, factory.getConfiguration)
      val xpath = factory.newXPath()
      xpath.setNamespaceContext(new LocalNamespaceContext(context))
      val expr = xpath.compile(query)

      expr.evaluate(doc, XPathConstants.NODESET).asInstanceOf[java.util.List[Node]]
    }
  }

  private class LocalNamespaceContext(lookup: Map[String, String]) extends NamespaceContext {
    def getPrefixes(uri: String) = throw new UnsupportedOperationException()
    def getPrefix(uri: String) = throw new UnsupportedOperationException()
    def getNamespaceURI(prefix: String) = prefix match {
      case p if lookup.containsKey(prefix) => lookup(p)
      case "xml" => "http://www.w3.org/XML/1998/namespace"
      case _ => ""
    }
  }

}