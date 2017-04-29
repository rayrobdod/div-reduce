scalaVersion := "2.10.6"


// Because SBT decides to ignore THIS ONE SPECIFIC SETTING when declared via Project::settings
scalastyleConfig := baseDirectory.value / ".." / ".." / "project" / "scalastyle-config.xml"


// scalaTest 
libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.0" % "test" 

testOptions in Test += Tests.Argument("-oS") 
