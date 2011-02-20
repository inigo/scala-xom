package net.surguy.xom

import xml.Node
import nu.xom.{Nodes => XomNodes, Node => XomNode}

/**
 * Implicit conversions for pimping the XOM XML library.
 *
 * @author Inigo Surguy
 * @created 20/02/2011 16:11
 */

object Implicits {

  implicit def PimpXomNode(node: XomNode) = new {
    def toScalaXml() = XomConverter.toScalaXml(node)

    def selectNodes(query: String)(implicit context:Map[String, String] = Map()): XomNodes =
      XomXPath.selectNodes(node, query)(context)

    def selectSingleNode(query: String)(implicit context:Map[String, String] = Map()): Option[XomNode] =
      XomXPath.selectSingleNode(node, query)(context)
  }

  implicit def PimpWithToXom(node: Node) = new {
    def toXom() = XomConverter.toXom(node)
  }

}