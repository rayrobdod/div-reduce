ThisBuild / version := "1.0-SNAPSHOT"
ThisBuild / organization := "com.rayrobdod"
ThisBuild / organizationHomepage := Some(new URL("http://rayrobdod.name/"))
ThisBuild / scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

val webStage = taskKey[Seq[File]]("Create a local directory with all the files laid out as they would be in the final distribution.")
val scala210Version = "2.10.7"
val scala211Version = "2.11.12"
val scala212Version = "2.12.17"
val scala213Version = "2.13.10"
val scala3Version = "3.2.2"

lazy val testSettings = Seq(
	libraryDependencies += "org.scalatest" %%% "scalatest" % "3.2.15" % "test",
)

lazy val shared = (projectMatrix in file("shared"))
	.settings(name := "div-reduce")
	.settings(testSettings:_*)
	.jvmPlatform(scalaVersions = Seq(
		scala210Version,
		scala211Version,
		scala212Version,
		scala213Version,
		scala3Version,
	))
	.jsPlatform(scalaVersions = Seq(
		scala212Version,
		scala213Version,
		scala3Version,
	))

lazy val console = (projectMatrix in file("console"))
	.dependsOn(shared)
	.settings(name := "div-reduce-console")
	.jvmPlatform(scalaVersions = Seq(
		scala210Version,
		scala211Version,
		scala212Version,
		scala213Version,
		scala3Version,
	))

lazy val plugin = project
	.enablePlugins(SbtPlugin)
	.dependsOn(shared.jvm(scala212Version))
	.settings(name := "div-reduce-plugin")
	.settings(
		addSbtPlugin("com.typesafe.sbt" % "sbt-web" % "1.4.4"),
		scriptedLaunchOpts := { scriptedLaunchOpts.value ++
			Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
		},
		scriptedBufferLog := false,
	)

lazy val webpage = (projectMatrix in file("webpage"))
	.enablePlugins(ScalaJSPlugin)
	.dependsOn(shared)
	.settings(name := "div-reduce-web")
	.settings(
		libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.4.0",
		watchSources += (Compile / sourceDirectory).value / "html" / "index.html",
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

			(webStage / mappings).value.map{case (srcFile, name) =>
				val tarFile = tarDir / name
				Files.copy(srcFile.toPath, tarFile.toPath, COPY_ATTRIBUTES, REPLACE_EXISTING)
				tarFile
			}
		},
	)
	.jsPlatform(scalaVersions = Seq(
		scala212Version,
		scala213Version,
		scala3Version,
	))

name := "aggregate"
