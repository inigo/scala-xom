package net.surguy.xom

import nu.xom.{Attribute => XomAttribute, Element => XomElement, Node => XomNode, Text => XomText, Comment => XomComment,
              ProcessingInstruction => XomProcessingInstruction, Document => XomDocument, Nodes => XomNodes}
import xml.{Elem, Node}

/**
 * Implicit conversions for pimping the XOM XML library, to make it easier to use from Scala.
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

    def nodes(): Seq[XomNode] = for (i <- (0 until node.getChildCount)) yield node.getChild(i)
  }

  implicit def PimpXomElement(e: XomElement) = new {
    def attributes(): Seq[XomAttribute] = for (i <- (0 until e.getAttributeCount)) yield e.getAttribute(i)

    def elements(): Seq[XomElement] = for (i <- (0 until e.getChildElements.size)) yield e.getChildElements.get(i)

    def namespaces(): Seq[Pair[String, String]] =
      for (i <- (0 until e.getNamespaceDeclarationCount)) yield (e.getNamespacePrefix(i), e.getNamespaceURI(e.getNamespacePrefix(i)))

    def toScalaXml(): Elem = XomConverter.toScalaXml(e).asInstanceOf[Elem]
  }

  implicit def PimpXomNodes(nodes: XomNodes) = new {
    def toSeq() = for (i <- (0 until nodes.size)) yield nodes.get(i)
  }

  implicit def PimpWithToXom(node: Node) = new {
    def toXom() = XomConverter.toXom(node)
  }

  implicit def PimpWithToXom(node: Elem) = new {
    def toXom(): XomElement = XomConverter.toXom(node).asInstanceOf[XomElement]
  }

}