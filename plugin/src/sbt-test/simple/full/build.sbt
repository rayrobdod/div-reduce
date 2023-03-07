version := "0.1"

Assets / divreduce / TaskKey[Unit]("assertResults") := {
	com.rayrobdod.divReduce.Functions.assertFileContentsEquals(
		(Assets / divreduce / target).value / "full.html",
		(Assets / resourceDirectory).value / "expected.html",
	)
}

WebKeys.stage / TaskKey[Unit]("assertResults") := {
	com.rayrobdod.divReduce.Functions.assertFileContentsEquals(
		WebKeys.stagingDirectory.value / "full.html",
		WebKeys.stagingDirectory.value / "expected.html",
	)
}

lazy val root = (project in file(".")).enablePlugins(SbtWeb)