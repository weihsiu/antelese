package com.netgents.antelese

import org.apache.tools.ant._
  import util._

class RichFileNameMapper(override val project: Project, val fileNameMapper: FileNameMapper) extends AntElement[FileNameMapper](project, fileNameMapper)
