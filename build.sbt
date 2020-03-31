val webStage = taskKey[Seq[File]]("")

lazy val shared = crossProject(JSPlatform, JVMPlatform).crossType(CrossType.Pure)
	.settings(name := "div-reduce")
	.settings(mySettings:_*)
	.settings(testSettings:_*)
	.settings(crossScalaVersions := Seq("2.10.7", "2.11.12", "2.12.11"))

lazy val console = project
	.dependsOn(shared.jvm)
	.settings(name := "div-reduce-console")
	.settings(mySettings:_*)
	.settings(crossScalaVersions := Seq("2.10.7", "2.11.12", "2.12.11"))

lazy val plugin = project
	.enablePlugins(SbtPlugin)
	.dependsOn(shared.jvm)
	.settings(name := "div-reduce-plugin")
	.settings(mySettings:_*)
	.settings(
		addSbtPlugin("com.typesafe.sbt" % "sbt-web" % "1.4.4"),
		scriptedLaunchOpts := { scriptedLaunchOpts.value ++
			Seq("-Xmx1024M", "-XX:MaxPermSize=256M", "-Dplugin.version=" + version.value)
		},
		scriptedBufferLog := false,
	)

lazy val webpage = project
	.enablePlugins(ScalaJSPlugin)
	.dependsOn(shared.js)
	.settings(name := "div-reduce-web")
	.settings(mySettings:_*)
	.settings(
		libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "1.0.0",
		watchSources += (sourceDirectory in Compile).value / "html" / "index.html",
		scalaJSUseMainModuleInitializer := true,
		(webStage / mappings) := Seq(
			(Compile / fullOptJS).value.data -> "script.js",
			((Compile / sourceDirectory).value / "html" / "index.html") -> "index.html"
		),
		webStage / target := (target.value / "web" / "stage"),
		webStage := {
			import java.nio.file.Files
			import java.nio.file.StandardCopyOption.{COPY_ATTRIBUTES, REPLACE_EXISTING}
			val tarDir = (webStage / target).value
			Files. createDirectories(tarDir.toPath)

			(mappings in webStage).value.map{case (srcFile, name) =>
				val tarFile = tarDir / name
				Files.copy(srcFile.toPath, tarFile.toPath, COPY_ATTRIBUTES, REPLACE_EXISTING)
				tarFile
			}
		},
	)

lazy val mySettings = Seq(
	organization := "com.rayrobdod",
	organizationHomepage := Some(new URL("http://rayrobdod.name/")),
	version := "1.0-SNAPSHOT",
	javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked"),
	scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
)

lazy val testSettings = Seq(
	libraryDependencies += "org.scalatest" %%% "scalatest" % "3.1.1" % "test",
	(Test / testOptions) += Tests.Argument("-oS"),
)

name := "aggregate"
