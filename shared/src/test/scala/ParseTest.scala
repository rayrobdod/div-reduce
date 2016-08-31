package com.rayrobdod.divReduce;

import java.util.Scanner
import org.scalatest.FunSpec;
import java.util.Arrays.asList

class ParseTest extends FunSpec {
	
	describe("divReduce.parse") {
		it ("produces and empty model for an empty input") {
			assertResult(Model()){
				parse(asList(""))
			}
		}
		it ("line comments start at a '#' and end at the end of a line") {
			assertResult(Model()){
				parse(asList("# This is a comment"))
			}
		}
		it ("treats a line prepended with '$' as script files") {
			assertResult(Model(scripts = Seq("/js/script.js"))){
				parse(asList("$/js/script.js"))
			}
		}
		it ("treats a second line prepended with '$' as script files") {
			assertResult(Model(scripts = Seq("/js/script.js", "/js/other.js"))){
				parse(asList("$/js/script.js","$/js/other.js"))
			}
		}
		it ("treats a line prepended with '!' as style files") {
			assertResult(Model(styles = Seq("/js/other.js"))){
				parse(asList("!/js/other.js"))
			}
		}
		it ("line comments can extist on the same line as commands") {
			assertResult(Model(styles = Seq("/js/script.js"))){
				parse(asList("!/js/script.js #very important file here"))
			}
		}
		it ("lines surrounded by '\"' are treated as text") {
			assertResult(Model(divTree = Div("", Seq(Text("abcd"))))){
				parse(asList("\"abcd\""))
			}
		}
		it ("lines without decoration are treated as divs") {
			assertResult(Model(divTree = Div("", Seq(Div("abcd", Nil))))){
				parse(asList("abcd"))
			}
		}
		it ("ignores empty lines") {
			assertResult(Model()){
				parse(asList("\n \n  \n\n \n\t\n\t\t\n".split("\n"):_*))
			}
		}
		it ("Treats indents as nesting") {
			assertResult(
				Model(divTree = Div("", Seq(
					Div("a", Seq(
						Div("b"), 
						Div("c")
					)),
					Div("d")
				)))
			){
				parse(asList("""
					|a
					| b
					| c
					|d""".stripMargin.replace("\r", "").split("\n"):_*
				))
			}
		}
		it ("Treats indents as nesting (2)") {
			assertResult(
				Model(divTree = Div("", Seq(
					Div("a", Seq(
						Div("b", Seq(Div("c"))),
						Div("d", Seq(Div("e")))
					)),
					Div("f"),
					Div("g", Seq(Div("h"), Div("i")))
				)))
			){
				parse(asList("""
					|a
					| b
					|  c
					| d
					|  e
					|f
					|g
					| h
					| i""".stripMargin.replace("\r", "").split("\n"):_*
				))
			}
		}
		it ("LongIndent") {
			assertResult(
				Model(divTree = Div("", Seq(
					Div("a",
						Seq(Div("",
							Seq(Div("",
								Seq(Div("",
									Seq(Div("",
										Seq(Div("b"))
									))
								))
							))
						))
					)
				)))
			){
				parse(asList("""a
					b""".replace("\r", "").split("\n"):_*
				))
			}
		}
		it ("Allows same-level index text and div combinations") {
			assertResult(
				Model(divTree = Div("", Seq(
					Div("a", Seq(
						Text("b"),
						Div("c"),
						Text("d")
					))
				)))
			){
				parse(asList("""
					|a
					| "b"
					| c
					| "d" """.stripMargin.replace("\r", "").split("\n"):_*
				))
			}
		}
		it ("Divs cannot be children of text elements") {
			assertResult(
				Model(divTree = Div("", Seq(
					Div("a", Seq(
						Text("b"),
						Div("", Seq(Div("c")))
					))
				)))
			){
				parse(asList("""
					|a
					| "b"
					|  c""".stripMargin.replace("\r", "").split("\n"):_*
				))
			}
		}
	}
}
