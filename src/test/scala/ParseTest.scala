package com.rayrobdod.divReduce;

import java.util.Scanner
import org.scalatest.FunSpec;

class ParseTest extends FunSpec {
	
	describe("divReduce.parse") {
		it ("produces and empty model for an empty input") {
			assertResult(Model()){
				parse(new Scanner(""))
			}
		}
		it ("line comments start at a '#' and end at the end of a line") {
			assertResult(Model()){
				parse(new Scanner("# This is a comment"))
			}
		}
		it ("treats a line prepended with '$' as script files") {
			assertResult(Model(scripts = Seq("/js/script.js"))){
				parse(new Scanner("$/js/script.js"))
			}
		}
		it ("treats a second line prepended with '$' as script files") {
			assertResult(Model(scripts = Seq("/js/script.js", "/js/other.js"))){
				parse(new Scanner("$/js/script.js\n$/js/other.js"))
			}
		}
		it ("treats a line prepended with '!' as style files") {
			assertResult(Model(styles = Seq("/js/other.js"))){
				parse(new Scanner("!/js/other.js"))
			}
		}
		it ("line comments can extist on the same line as commands") {
			assertResult(Model(styles = Seq("/js/script.js"))){
				parse(new Scanner("!/js/script.js #very important file here"))
			}
		}
		it ("lines surrounded by '\"' are treated as text") {
			assertResult(Model(divTree = Div("", Seq(Text("abcd"))))){
				parse(new Scanner("\"abcd\""))
			}
		}
		it ("lines without decoration are treated as divs") {
			assertResult(Model(divTree = Div("", Seq(Div("abcd", Nil))))){
				parse(new Scanner("abcd"))
			}
		}
		it ("ignores empty lines") {
			assertResult(Model()){
				parse(new Scanner("\n \n  \n\n \n\t\n\t\t\n"))
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
				parse(new Scanner("""
					|a
					| b
					| c
					|d""".stripMargin
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
				parse(new Scanner("""
					|a
					| b
					|  c
					| d
					|  e
					|f
					|g
					| h
					| i""".stripMargin
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
				parse(new Scanner("""a
					b"""
				))
			}
		}
	}
}
