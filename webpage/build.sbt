scalaVersion := "2.10.6"

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.0"


// Because SBT decides to ignore THIS ONE SPECIFIC SETTING when declared via Project::settings
scalastyleConfig := baseDirectory.value / ".." / "project" / "scalastyle-config.xml"


watchSources += ((sourceDirectory in Compile).value / "html" / "index.html")

val webStage = taskKey[Seq[File]]("")

target in webStage := target.value / "web" / "stage"

mappings in webStage := Seq(
	(fullOptJS in Compile).value.data -> "script.js",
	((sourceDirectory in Compile).value / "html" / "index.html") -> "index.html"
)

webStage := {
	import java.nio.file.Files
	import java.nio.file.StandardCopyOption.{COPY_ATTRIBUTES, REPLACE_EXISTING} 
	val tarDir = (target in webStage).value
	Files. createDirectories(tarDir.toPath)
	
	(mappings in webStage).value.map{case (srcFile, name) =>
		val tarFile = tarDir / name
		Files.copy(srcFile.toPath, tarFile.toPath, COPY_ATTRIBUTES, REPLACE_EXISTING)
		tarFile
	}
}
