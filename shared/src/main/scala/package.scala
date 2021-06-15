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
package com.rayrobdod

package divReduce {
	final case class Settings(
	)
	
	final case class Model(
		styles:Seq[String] = Nil,
		scripts:Seq[String] = Nil,
		divTree:Div = Div()
	)
	
	abstract sealed trait DivTree
	
	final case class Div(
		clazz:String = "",
		children:Seq[DivTree] = Nil
	) extends DivTree {
		def append(depth:Int, value:DivTree):Div = {
			if (depth == 0) {
				this.copy(clazz, children :+ value)
			} else {
				if (children.length == 0) {
					this.copy(clazz, children = Seq(new Div().append(depth - 1, value)))
				} else {
					children.last match {
						case Text(_) => {
							this.copy(clazz, children = this.children :+ new Div().append(depth - 1, value))
						}
						case x:Div => {
							this.copy(clazz, children = this.children.init :+ x.append(depth - 1, value))
						}
					}
				}
			}
		}
	}
	
	final case class Text(
		value:String
	) extends DivTree
}

package object divReduce {
	
	/** Read a series of lines in rrd-divreduce format and produce a [[com.rayrobdod.divReduce.Model]] */
	def parse(in:java.lang.Iterable[String], s:Settings = Settings()):Model = {
		var retVal:Model = Model();
		val in2 = in.iterator();
		
		while(in2.hasNext) {
			val lineFull = in2.next();
			val line = lineFull.split('#').apply(0);
			
			
			line.trim.headOption match {
				case None => {
					// do nothing; ignore blank lines
				}
				case Some('!') => {
					retVal = retVal.copy(styles = retVal.styles :+ line.trim.tail)
				}
				case Some('$') => {
					retVal = retVal.copy(scripts = retVal.scripts :+ line.trim.tail)
				}
				case Some('"') => {
					val depth = line.takeWhile{x => (x == ' ') || (x == '\t')}.size
					// TODO: error if last character after trim is not '"'
					retVal = retVal.copy(divTree = retVal.divTree.append(depth, new Text(line.trim.tail.init)))
				}
				case _ => {
					val depth = line.takeWhile{x => (x == ' ') || (x == '\t')}.size
					retVal = retVal.copy(divTree = retVal.divTree.append(depth, new Div(line.trim)))
				}
			}
		}
		
		retVal;
	}
	
	/** Convert a model into a string in html format */
	def toHtml(m:Model, s:Settings = Settings()):String = {
		"""<!DOCTYPE html>
		|<html>
		|<head>
		|""".replace("\r","").stripMargin +
		m.styles.map({_.flatMap(htmlEscape).mkString("\t<link rel='stylesheet' href='", "", "' />\n")}).mkString("", "", "") +
		m.scripts.map({_.flatMap(htmlEscape).mkString("\t<script src='", "", "' ></script>\n")}).mkString("", "", "") +
		"</head>\n<body>\n" +
		toHtml(m.divTree, "\t", s) +
		"</body>\n</html>"
	}
	
	private[this] def toHtml(d:Div, indent:String, s:Settings):String = {
		indent +
		"<div" +
		(if (d.clazz != "") {d.clazz.flatMap(htmlEscape).mkString(" class='", "", "'")} else {""}) +
		(if (d.children.isEmpty) {
			" />"
		} else {
			">\n" +
			d.children.map{x => x match {
				case Text(str) => str.flatMap(htmlEscape).mkString(indent + "\t", "", "\n")
				case x:Div => toHtml(x, indent + "\t", s).mkString
			}}.mkString("", "", "") +
			indent +
			"</div>"
		}) +
		"\n"
	}
	
	private[this] val htmlEscape:Function1[Char, Seq[Char]] = {(c:Char) => c match {
		case '\"' => "&quot;"
		case '\'' => "&apos;"
		case '&' => "&amp;"
		case '<' => "&lt;"
		case '>' => "&gt;"
		case x => x :: Nil
	}}
}
