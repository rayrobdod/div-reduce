# div-reduce

[![Build Status](https://travis-ci.org/rayrobdod/div-reduce.svg?branch=master)](https://travis-ci.org/rayrobdod/div-reduce)


Have you, as a professional web developer, ever looked at one of your html files
and wondered why there are so many `<div class="">`? All the divs and classes
are just boilerplate. The only three html elements a true web dev needs are
`link type=stylesheet`, `script` and `div`, and as far as a true web developer
knows, those are the only three html elements that exist. Why use `<img src=''>`
when `background-image` exists in CSS, and why use `<a href=''>` when emcascript
has `window.location = ''`? 

This project seeks to accommodate true web developers by removing the boilerplate.
Defined within is a lightweight markup syntax that can be preprocessed into
html. 

## Document Syntax

At its core, the document structure is a line-per-element document, where the
text of the line corresponds to a div's class attribute, and the indentation
indicates nesting. So, for example:

```
table
	row
		cell label
		cell
```

translates into

```html
<div class="table">
	<div class="row">
		<div class="cell label" />
		<div class="cell" />
	</div>
</div>
```

Now, unfortunately, there are some things that cannot be performed with divs
alone. The CSS Generated Content Module is still languishing in development hell,
so text has to be included in the html/divless document. To do so, put quotes
around the text and indent appropriately. Since there will be many more divs in
any document than text elements, it makes sense to make text elements more
verbose. At this time, text is not capable of spanning multiple lines, but if
you somehow have a text element longer than a few words, adjacent text lines
will be concatenated.

```
container
	header
		"name of site"
	main
		"words words"
		"words words"
		"words words"
```

```html
<div class="container">
	<div class="header">
		name of site
	</div>
	<div class="main">
		words words
		words words
		words words
	</div>
</div>
```

Also unfortunately, but I am not a true web dev so there might be something I
don't know about, there is no javascript css parser. So, you will need style
links to get them beautiful frameworks onto your page. A line prepended with a
'!' will be treated as the URI of a stylesheet to use for the current document.
Multiple stylesheets are allowed in each document. Regardless of position or
indentation, these will be placed in the head of the resulting document.

I am willing to accept suggestions for a better character to indicate 'styles'
than '!', but it should be one of the characters not allowed in class names.

```
!stylesheet.css
```

```html
<link rel="stylesheet" href="stylesheet.css" />
```

Also unfortunately, scripts cannot come in div form, as of yet. Yes, that is a
challenge. A line prepended with a '$' will be treated as the URI of a script to
use for the current document. Multiple scripts are allowed in each document.
Regardless of position or indentation, these will be placed in the head of the
resulting document.

The '$' is non-negotiable.

```
$https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js
$js/my_script.js
```

```html
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src="js/my_script.js"></script>
```

And because why not, '#' start line comments. Comments are ignored by the
parser, and are not translated into equivalent html comments.

```
# a metroui icon
mif-medkit mif-2x fg-green # large, green medkit
```

```html
<div class="mif-earth mif-2x fg-green" />
```

## Build Instructions

Build with [sbt](http://www.scala-sbt.org/).

There are five subprojects.
* `shared` library sources for scala on the jvm
* `shared` library sources for scalajs
* `console` a command-line program which reads a divreduce file and writes a
  html file
* `plugin` a [sbt-web](https://github.com/sbt/sbt-web) plugin providing a
  `divreduce` source file task and a set of options scoped to that task. The
  default options are to read files in the assets directory with the
  ".rrd-divreduce" extension and write corresponding html files to the
  sourceManaged directory
* `webpage`a demo html page with a textarea that accepts divreduce text and an
  output that prints the post-conversion html

## Justification

And just in case you think I am building a strawman: web-frameworks expect you
to do exactly this, most web-icon sets don't hand you images and expect you to
know what `::after` is, they expect you to put an empty div with classes in your
html, and probably most importantly, inexcusably and straw-camel-back-breakingly,
[seeing someone in meatspace with no intention of framework-making convert
content-important img tags to divs-with-background-image](https://github.com/dxa4481/Veyebrations_website/commit/30366b1b7b830dbfdad52efd172d760d084d28d7).
