scalaVersion := "2.10.6"

crossScalaVersions := Seq("2.10.6", "2.11.8")


// Because SBT decides to ignore THIS ONE SPECIFIC SETTING when declared via Project::settings
scalastyleConfig := baseDirectory.value / ".." / ".." / "project" / "scalastyle-config.xml"


// scalaTest 
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test" 

testOptions in Test += Tests.Argument("-oS") 
