package com.netgents.antelese

import org.apache.tools.ant._
  import filters._

class RichBaseFilterReader(override val project: Project, val baseFilterReader: BaseFilterReader) extends AntElement[BaseFilterReader](project, baseFilterReader)
