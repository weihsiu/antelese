package com.netgents.antelese

import java.lang.reflect._

object ClassUtil {

  implicit def enrichClass[A](clazz: Class[A]) = new RichClass[A](clazz)
}

class RichClass[A](clazz: Class[A]) {
  
  import Predefs._

  def getMethodsByName(name: String): List[Method] = clazz.getMethods filter (_.getName == name) toList

  def getOptionalMethod(name: String, parameterTypes: Class[_]*): Option[Method] = clazz.getMethod(name, parameterTypes: _*)
}
