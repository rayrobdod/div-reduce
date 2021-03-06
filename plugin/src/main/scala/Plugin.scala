/*
	The MIT License (MIT)
	
	Copyright (c) 2015-2016 Raymond Dodge
	
	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:
	
	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.
	
	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE.
*/
package com.rayrobdod.divReduce

import sbt._
import Keys._
import com.typesafe.sbt.web.SbtWeb
import com.typesafe.sbt.web.Import.WebKeys.webTarget
import com.typesafe.sbt.web.Import.Assets
import java.nio.file.{Paths, Files}
import java.nio.file.StandardOpenOption.CREATE
import java.nio.charset.StandardCharsets.UTF_8

object Functions {
	
	/** Test whether the two files are equivalent.
	 * 
	 * If this returns normally, the file contents are equal;
	 * if this throws,then the file contents are not equal
	 * 
	 * used repeatedly in sbt-scripted-tests.
	 */
	def assertFileContentsEquals(exp:File, res:File):Unit = {
		if (! exp.exists()) {sys.error("source file does not exist")}
		if (! res.exists()) {sys.error("result file does not exist")}
		
		if (exp.isDirectory) {
			if (res.isDirectory) { /* OK */ }
			else {sys.error("source was directory; result was not")}
		} else {
			if (res.isDirectory) {sys.error("result was directory; source was not")}
			else {
				val expLines = java.nio.file.Files.readAllLines(exp.toPath)
				val resLines = java.nio.file.Files.readAllLines(res.toPath)
				if (expLines != resLines) {sys.error("source and result file have different contents: ")}
				
				// else, OK
			}
		}
	}
	
}

object Plugin extends AutoPlugin {
	object autoImport {
		val divreduce = taskKey[Seq[File]]("Convert divreduce files to html")
	}
	import autoImport._
	override lazy val projectSettings = Seq(
		Assets / divreduce / sourceDirectory := (Assets / sourceDirectory).value,
		Assets / divreduce / target := (Assets / sourceManaged).value,
		Assets / divreduce / includeFilter := {
			new FileFilter{
				def accept(f:File) = (f.toString endsWith ".rrd-divreduce")
			}
		},
		Assets / divreduce / sources := {
			(Assets / divreduce / sourceDirectory).value **
					((Assets / divreduce / includeFilter).value --
					(Assets / divreduce / excludeFilter).value)
		}.get,
		(Assets / divreduce) := {
			(Assets / divreduce / sources).value.map{fileName:File =>
				val baseDir = (Assets / divreduce / sourceDirectory).value.toPath
				
				val input = Files.readAllLines(fileName.toPath, UTF_8)
				val output = toHtml(parse(input))
				val outputFile = new File((Assets / divreduce / target).value, IO.split(IO.relativize(baseDir.toFile, fileName).get)._1 + ".html").toPath
				
				IO.createDirectory(outputFile.toFile.getParentFile)
				Files.write(outputFile, java.util.Arrays.asList(output), UTF_8, CREATE)
				outputFile.toFile
			}.filter{_ != null}
		},
		Assets / sourceGenerators += (Assets / divreduce).taskValue
	)
	
	override def requires = SbtWeb
	override def trigger = allRequirements
}
