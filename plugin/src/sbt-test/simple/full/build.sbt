version := "0.1"

TaskKey[Unit]("assertResults") in divreduce in Assets := {
	com.rayrobdod.divReduce.Functions.assertFileContentsEquals(
		(target in divreduce in Assets).value / "full.html",
		(resourceDirectory in Assets).value / "expected.html",
	)
}

TaskKey[Unit]("assertResults") in WebKeys.stage := {
	com.rayrobdod.divReduce.Functions.assertFileContentsEquals(
		WebKeys.stagingDirectory.value / "full.html",
		WebKeys.stagingDirectory.value / "expected.html",
	)
}

lazy val root = (project in file(".")).enablePlugins(SbtWeb)