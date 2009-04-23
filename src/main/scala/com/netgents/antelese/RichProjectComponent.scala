package com.netgents.antelese

import org.apache.tools.ant._
  import types._

class RichDataType(val dataType: DataType) extends RichProjectComponent[DataType](dataType)

class RichTask(val task: Task) extends RichProjectComponent[Task](task) {

  override def apply(attributes: (Symbol, Any)*): AntElement[Task] = {
    if (attributes.contains('perform -> false))
      super.apply((attributes.toList - ('perform -> false)): _*)
    else {
      val richTask = super.apply(attributes: _*)
      richTask.configure
      richTask.getAntElement.asInstanceOf[Task].perform
      richTask
    }
  }
}

class RichProjectComponent[A <: ProjectComponent](val projectComponent: A) extends AntElement[A](projectComponent.getProject, projectComponent)
