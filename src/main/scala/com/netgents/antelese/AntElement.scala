package com.netgents.antelese

import org.apache.tools.ant._
  import filters.{util => _, _}
  import types._
    import resources.comparators._
  import util._

class AntElement[A <: AnyRef](val project: Project, val antElement: A) {

  import ClassUtil._
  import java.io.File
  import java.lang.reflect.Method

  private var attributes: List[(Symbol, Any)] = _

  def getAntElement = antElement.asInstanceOf[Object]

  def apply[B <: AnyRef](value: AntElement[B]): AntElement[A] = apply(List(value))

  def apply(values: List[AntElement[_]]): AntElement[A] = apply((values map (('<>, _))): _*)

  def apply(attributes: (Symbol, Any)*): AntElement[A] = {
    this.attributes = attributes.toList - Antelese.ignore
    this
  }

  def configure = attributes foreach { case (symbol, value) => setAttribute(antElement, symbol.toString.drop(1), value) }
    
  override def toString = super.toString + ", antElement = " + antElement.toString

  protected def setAttribute(obj: AnyRef, name: String, value: Any) {
    val introspector = IntrospectionHelper.getHelper(project, obj.getClass)
    if (name != "<>" && value.isInstanceOf[String])
      introspector.setAttribute(project, obj, name, project.replaceProperties(value.toString))
    else {
      getMethod(obj, name, value) match {
        case Some(method) =>
          if (method.getName.startsWith("set") && value.isInstanceOf[AntElement[_]]) // configure before "setXXX()"
            value.asInstanceOf[AntElement[_]].configure
          method.invoke(obj,
                        if (value.isInstanceOf[AntElement[_]])
                          value.asInstanceOf[AntElement[_]].getAntElement
                        else
                          value.asInstanceOf[Object])
          if (method.getName.startsWith("add") && value.isInstanceOf[AntElement[_]]) // configure after "add()"
            value.asInstanceOf[AntElement[_]].configure
        case None =>
          try {
            val elementMethod = introspector.getElementMethod(name)
            invokeElementMethod(obj, elementMethod, value)
          }
          catch {
            case (_: BuildException) =>
              setAttribute(obj, "<>", value) // try default
            case exception =>
              throw new IllegalArgumentException("invalid property " + name, exception)
          }
      }
    }
  }

  private def getMethod(obj: AnyRef, name: String, value: Any): Option[Method] = {
    if (name == "<>") {
      val v = if (value.isInstanceOf[AntElement[_]]) value.asInstanceOf[AntElement[_]].getAntElement else value
      v match {
        case _: ChainableReader =>
          obj.getClass.getOptionalMethod("add", classOf[ChainableReader])
        case _: FileNameMapper =>
          obj.getClass.getOptionalMethod("add", classOf[FileNameMapper])
        case _: ResourceComparator =>
          obj.getClass.getOptionalMethod("add", classOf[ResourceComparator])
        case _: ResourceCollection =>
          obj.getClass.getOptionalMethod("add", classOf[ResourceCollection])
        case _: String =>
          obj.getClass.getOptionalMethod("addText", classOf[java.lang.String])
        case _: Task =>
          obj.getClass.getOptionalMethod("addTask", classOf[Task])
        case _ =>
          None
      }
    }
    else {
      obj.getClass getMethodsByName("set" + name.capitalize) match {
        case List(method) if method.getParameterTypes.length == 1 =>
          Some(method)
        case _ =>
          None
      }
    }
  }

  private def invokeElementMethod(obj: AnyRef, elementMethod: Method,  value: Any) {
    if (elementMethod.getName.startsWith("addConfigured") && value.isInstanceOf[AntElement[_]]) { // configure before "addConfiguredXXX()"
      value.asInstanceOf[AntElement[_]].configure
      elementMethod.invoke(obj, value.asInstanceOf[AntElement[_]].getAntElement)
    }
    else if (elementMethod.getName.startsWith("add") && value.isInstanceOf[AntElement[_]]) { // configure after "addXXX()"
      elementMethod.invoke(obj, value.asInstanceOf[AntElement[_]].getAntElement)
      value.asInstanceOf[AntElement[_]].configure
    }
    else if (elementMethod.getName.startsWith("create")) {
      val element = elementMethod.invoke(obj)
      val attributes =
        if (value.isInstanceOf[(Symbol, Any)])
          List(value.asInstanceOf[(Symbol, Any)])
        else if (value.isInstanceOf[List[(Symbol, Any)]])
          value.asInstanceOf[List[(Symbol, Any)]]
        else
          throw new IllegalArgumentException("invalid value '%s' to apply to object '%s' created with method '%s'".format(value, element, elementMethod))
      attributes foreach { case (symbol, value) => setAttribute(element, symbol.toString.drop(1), value) }
    }
    else
      throw new IllegalArgumentException("invalid method: " + elementMethod.toString)
  }
}
