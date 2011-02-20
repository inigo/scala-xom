package net.surguy.xom

import xml._
import nu.xom.{Attribute => XomAttribute, Element => XomElement, Node => XomNode, Text => XomText, Comment => XomComment,
              ProcessingInstruction => XomProcessingInstruction, Document => XomDocument}

/**
 * Convert Scala XML to the XOM XML model, and vice versa.
 *
 * @author Inigo Surguy
 * @created 20/02/2011 12:20
 */
object XomConverter {

  def toXom(node: Node) : XomNode = node match {
    case Elem(prefix, label, attributes, scope, children @ _*) =>
      val xomElement = if (scope.getURI(prefix)==null) new XomElement(label) else new XomElement(prefixed(prefix,label), scope.getURI(prefix))
      for (att <- attributes) att match {
        case a: PrefixedAttribute => xomElement.addAttribute(new XomAttribute(a.prefixedKey, scope.getURI(a.pre), a.value.text))
        case a: UnprefixedAttribute => xomElement.addAttribute(new XomAttribute(a.key, a.value.text))
      }
      children.foreach(e => xomElement.appendChild(toXom(e)))
      xomElement
    case Text(text) => new XomText(text)
    case Comment(text) => new XomComment(text)
    case ProcInstr(target, data) => new XomProcessingInstruction(target, data)
    case entity: EntityRef => new XomText(entity.text)
    case PCData(text) => new XomText(text) // XOM does not support PCDATA - this is the closest alternative
    case Group(nodes) => throw new UnsupportedOperationException("Converting groups is not supported") // @todo Convert Group to NodeList?
    case Unparsed(text) => throw new UnsupportedOperationException("XOM does not support unparsed entities")
    case _ => throw new UnsupportedOperationException("Cannot convert Scala "+node+" type "+node.getClass.getName+" to XOM")
  }

  private def prefixed(prefix: String, label: String) = if (prefix==null) label else prefix+":"+label

  def toScalaXml(node: XomNode): Node = node match {
    case n: XomDocument => toScalaXml(n.getRootElement) // @todo This is losing any non-element content e.g. an initial comment
    case n: XomElement =>
      val xomAttributes = for (i <- (0 until n.getAttributeCount)) yield n.getAttribute(i)
      val attributes = xomAttributes.foldLeft(Null.asInstanceOf[MetaData])((existing, a) =>
        Attribute(a.getNamespacePrefix, a.getLocalName, Text(a.getValue), existing ))
      val xomChildren = for (i <- (0 until n.getChildCount)) yield toScalaXml(n.getChild(i))
      new Elem(if (n.getNamespacePrefix=="") null else n.getNamespacePrefix, n.getLocalName, attributes, TopScope, xomChildren :_* )
    case n: XomText => Text(n.getValue)
    case n: XomComment => Comment(n.getValue)
    case n: XomProcessingInstruction => ProcInstr(n.getTarget, n.getValue)
    case _ => throw new UnsupportedOperationException("Cannot convert XOM "+node+" type "+node.getClass.getName+" to Scala XML")
  }

}