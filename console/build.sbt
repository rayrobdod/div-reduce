mainClass in Compile := Some("com.rayrobdod.divReduce.Runner")

scalaVersion := "2.10.6"


// Because SBT decides to ignore THIS ONE SPECIFIC SETTING when declared via Project::settings
scalastyleConfig := baseDirectory.value / ".." / "project" / "scalastyle-config.xml"
