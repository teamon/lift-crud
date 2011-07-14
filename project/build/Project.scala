import sbt._

class Project(info: ProjectInfo) extends DefaultWebProject(info) {
    val liftVersion = "2.4-M1"

    // uncomment the following if you want to use the snapshot repo
    val scalatoolsSnapshot = ScalaToolsSnapshots

    override def libraryDependencies = Set(
        "net.liftweb" %% "lift-webkit" % liftVersion % "compile",
        "org.mortbay.jetty" % "jetty" % "6.1.22" % "test",
        "ch.qos.logback" % "logback-classic" % "0.9.26"
    ) ++ super.libraryDependencies
}
