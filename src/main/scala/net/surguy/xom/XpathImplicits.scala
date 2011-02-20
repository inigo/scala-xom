package net.surguy.xom

import net.sf.saxon.xom.DocumentWrapper
import net.sf.saxon.xpath.XPathFactoryImpl
import nu.xom.{Nodes, Node}
import javax.xml.xpath.XPathConstants
import scala.collection.JavaConversions._
import javax.xml.namespace.NamespaceContext
import java.lang.String

/**
 * Add the XPath 2 methods to the XOM node via implicits.
 *
 * @author Inigo Surguy
 * @created 19/02/2011 18:11
 */
object XpathImplicits {
  implicit def PimpWithXPath(node: Node) = new {
    def selectNodes(query: String)(implicit context:Map[String, String] = Map()): Nodes =
      XomXPath.selectNodes(node, query)(context)

    def selectSingleNode(query: String)(implicit context:Map[String, String] = Map()): Option[Node] =
      XomXPath.selectSingleNode(node, query)(context)
  }
}

/**
 * Make XPath2 available in XOM, via Saxon.
 *
 * @author Inigo Surguy
 * @created 19/02/2011 18:11
 */
object XomXPath {
  def selectNodes(node: Node, query: String)(implicit context:Map[String, String] = Map()): Nodes = {
    val nodes = runQuery(node, query, context)
    nodes.foldLeft(new Nodes())((xomNodes, node) => { xomNodes.append(node); xomNodes } )
  }

  def selectSingleNode(node: Node, query: String)(implicit context:Map[String, String] = Map()): Option[Node] =
    runQuery(node, query, context).headOption

  private def runQuery(node: Node, query: String, context: Map[String, String]): java.util.List[Node] = {
    val factory = new XPathFactoryImpl()
    val doc = new DocumentWrapper(node, null, factory.getConfiguration)
    val xpath = factory.newXPath()
    xpath.setNamespaceContext(new LocalNamespaceContext(context))
    val expr = xpath.compile(query)

    expr.evaluate(doc, XPathConstants.NODESET).asInstanceOf[java.util.List[Node]]
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