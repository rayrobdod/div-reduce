package com.rayrobdod.divReduce;

import java.util.Scanner
import org.scalatest.FunSpec;

class ToHtmlTest extends FunSpec {
	
	describe("divReduce.toHtml") {
		it ("Produces an empty html document for an empty document") {
			assertResult("""<!DOCTYPE html>
			|<html>
			|<head>
			|</head>
			|<body>
			|	<div />
			|</body>
			|</html>""".replace("\r","").stripMargin){
				toHtml(new Model())
			}
		}
		it ("Creates `link rel='stylesheet'` elements in response the model's styles elem") {
			assertResult("""<!DOCTYPE html>
			|<html>
			|<head>
			|	<link rel='stylesheet' href='style.css' />
			|</head>
			|<body>
			|	<div />
			|</body>
			|</html>""".replace("\r","").stripMargin){
				toHtml(new Model(styles = Seq("style.css")))
			}
		}
		it ("Creates `link rel='stylesheet'` elements in response the model's styles elem (2)") {
			assertResult("""<!DOCTYPE html>
			|<html>
			|<head>
			|	<link rel='stylesheet' href='style.css' />
			|	<link rel='stylesheet' href='blarg.css' />
			|</head>
			|<body>
			|	<div />
			|</body>
			|</html>""".replace("\r","").stripMargin){
				toHtml(new Model(styles = Seq("style.css", "blarg.css")))
			}
		}
		it ("Creates `script` elements in response the model's styles elem") {
			assertResult("""<!DOCTYPE html>
			|<html>
			|<head>
			|	<script src='script.js' ></script>
			|</head>
			|<body>
			|	<div />
			|</body>
			|</html>""".replace("\r","").stripMargin){
				toHtml(new Model(scripts = Seq("script.js")))
			}
		}
		it ("Creates `script` elements in response the model's styles elem (2)") {
			assertResult("""<!DOCTYPE html>
			|<html>
			|<head>
			|	<script src='script.js' ></script>
			|	<script src='blarg.js' ></script>
			|</head>
			|<body>
			|	<div />
			|</body>
			|</html>""".replace("\r","").stripMargin){
				toHtml(new Model(scripts = Seq("script.js", "blarg.js")))
			}
		}
		it ("do the divtree thing") {
			assertResult("""<!DOCTYPE html>
			|<html>
			|<head>
			|</head>
			|<body>
			|	<div>
			|		<div class='a'>
			|			<div class='b'>
			|				<div class='c' />
			|			</div>
			|			<div class='d'>
			|				e
			|			</div>
			|		</div>
			|		<div class='f' />
			|		<div class='g'>
			|			h
			|			<div class='i' />
			|		</div>
			|	</div>
			|</body>
			|</html>""".replace("\r","").stripMargin){
				toHtml(new Model(divTree = Div("", Seq(
					Div("a", Seq(
						Div("b", Seq(Div("c"))),
						Div("d", Seq(Text("e")))
					)),
					Div("f"),
					Div("g", Seq(Text("h"), Div("i")))
				))))
			}
		}
	}
}
