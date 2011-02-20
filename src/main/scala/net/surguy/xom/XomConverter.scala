package net.surguy.xom

import xml._
import nu.xom.{Attribute => XomAttribute, Element => XomElement, Node => XomNode, Text => XomText, Comment => XomComment,
              ProcessingInstruction => XomProcessingInstruction}

/**
 * Convert Scala XML to the XOM XML model, and vice versa.
 *
 * @author Inigo Surguy
 * @created 20/02/2011 12:20
 */
object XomConverter {

  def toXom(node: Node) : XomNode = node match {
    case Elem(prefix, label, attributes, scope, children @ _*) =>
      val xomElement = if (prefix==null) new XomElement(label) else new XomElement(prefix+":"+label, scope.getURI(prefix))
      attributes.foreach(a => xomElement.addAttribute(new XomAttribute(a.key, a.value.text)))
      children.foreach(e => xomElement.appendChild(toXom(e)))
      xomElement
    case Text(text) => new XomText(text)
    case Comment(text) => new XomComment(text)
    case ProcInstr(target, data) => new XomProcessingInstruction(target, data)
    case entity: EntityRef => new XomText(entity.text)
    case PCData(text) => new XomText(text) // XOM does not support PCDATA - this is the closest alternative
    case Group(nodes) => throw new UnsupportedOperationException("Converting groups is not supported") // @todo Convert Group to NodeList?
    case Unparsed(text) => throw new UnsupportedOperationException("XOM does not support unparsed entities")
    case _ => throw new UnsupportedOperationException("Cannot convert "+node+" type "+node.getClass.getName+" to XOM")
  }

}