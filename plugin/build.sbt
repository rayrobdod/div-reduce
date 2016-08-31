sbtPlugin := true

addSbtPlugin("com.typesafe.sbt" % "sbt-web" % "1.4.0")


// Because SBT decides to ignore THIS ONE SPECIFIC SETTING when declared via Project::settings
scalastyleConfig := baseDirectory.value / ".." / "project" / "scalastyle-config.xml"
