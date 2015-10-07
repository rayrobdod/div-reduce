/*
The MIT License (MIT)

Copyright (c) 2015 Raymond Dodge

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

import java.nio.file.{Paths, Files}
import java.nio.file.StandardOpenOption.CREATE
import java.nio.charset.StandardCharsets.UTF_8

object Runner {
	/**
	 * @param args[0] the input divreduce file to process
	 * @param args[1] the location to write the output html file to
	 */
	def main(args:Array[String]):Unit = {
		if (args.length < 2) {
			System.out.println("java -jar div-reduce.jar <inputfile> <outputfile>")
		} else {
			val inputFile = Paths.get(args(0));
			val outputFile = Paths.get(args(1));
			
			val input = new java.util.Scanner(Files.newBufferedReader(inputFile, UTF_8))
			val output = toHtml(parse(input))
			Files.createDirectories(outputFile.getParent)
			Files.write(outputFile, java.util.Arrays.asList[String](output), UTF_8, CREATE)
		}
	}
}
