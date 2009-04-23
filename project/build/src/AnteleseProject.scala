import sbt._

class AnteleseProject(info: ProjectInfo) extends DefaultProject(info) {

  val ant = "org.apache.ant" % "ant" % "1.7.1"
  val antLauncher = "org.apache.ant" % "ant-launcher" % "1.7.1"
  val antApacheRegexp = "org.apache.ant" % "ant-apache-regexp" % "1.7.1"
  val specs = "org.specs" % "specs" % "1.4.3"
}
