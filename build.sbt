sbtPlugin := true

name := "div-reduce"

organization := "com.rayrobdod"

organizationHomepage := Some(new URL("http://rayrobdod.name/"))

version := "a.0-SNAPSHOT"

javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

mainClass in Compile := Some("com.rayrobdod.divReduce.Runner")

addSbtPlugin("com.typesafe.sbt" % "sbt-web" % "1.2.2")

scalastyleConfig := baseDirectory.value / "project" / "scalastyle-config.xml"

// scalaTest 
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.5" % "test" 

testOptions in Test += Tests.Argument("-oS") 
