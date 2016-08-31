// Why use a configuration when one can have a new project?
// It''s not like this is the canonical reason to have multiple configurations.
// Although SBT configurations don't seem to support one being a sbt plugin and the others not being a sbt plugin

lazy val shared = crossProject.crossType(SharedCrossType)
	.settings(name := "div-reduce")
	.settings(mySettings:_*)

// Needed, so sbt finds the projects
lazy val sharedJVM = shared.jvm
lazy val sharedJS = shared.js


lazy val console = project
	.dependsOn(sharedJVM)
	.settings(name := "div-reduce-console")
	.settings(mySettings:_*)

lazy val plugin = project
	.dependsOn(sharedJVM)
	.settings(name := "div-reduce-plugin")
	.settings(mySettings:_*)

lazy val webpage = project
	.enablePlugins(ScalaJSPlugin)
	.dependsOn(sharedJS)
	.settings(name := "div-reduce-web")
	.settings(mySettings:_*)

lazy val mySettings = Seq(
	organization := "com.rayrobdod",
	organizationHomepage := Some(new URL("http://rayrobdod.name/")),
	version := "a.0-SNAPSHOT",
	javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked"),
	scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
)

name := "aggregate"

scalastyleConfig := baseDirectory.value / "project" / "scalastyle-config.xml"

crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.0-M4")
