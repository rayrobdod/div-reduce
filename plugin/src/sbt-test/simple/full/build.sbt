version := "0.1"

TaskKey[Unit]("assertResults") in divreduce in Assets := {
	import java.io._
	
	val res = sbt.IO.readBytes((target in divreduce in Assets).value / "full.html")
	val exp = sbt.IO.readBytes((resourceDirectory in Assets).value / "expected.html")
	assert(res.size == exp.size)
	assert(res.zip(exp).forall{x => x._1 == x._2})
}

TaskKey[Unit]("assertResults") in WebKeys.stage := {
	import java.io._
	
	val res = sbt.IO.readBytes(WebKeys.stagingDirectory.value / "full.html")
	val exp = sbt.IO.readBytes(WebKeys.stagingDirectory.value / "expected.html")
	assert(res.size == exp.size)
	assert(res.zip(exp).forall{x => x._1 == x._2})
}

lazy val root = (project in file(".")).enablePlugins(SbtWeb)