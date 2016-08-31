{
  val pluginVersion = System.getProperty("plugin.version")
  if(pluginVersion == null) {
    throw new RuntimeException("""|The system property 'plugin.version' is not defined.
                                  |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
  } else {
    addSbtPlugin("com.rayrobdod" % "div-reduce-plugin" % pluginVersion)
  }
}

addSbtPlugin("com.typesafe.sbt" % "sbt-web" % "1.4.0")
