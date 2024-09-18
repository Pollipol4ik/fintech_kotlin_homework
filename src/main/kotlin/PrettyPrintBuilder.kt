class PrettyPrintHTMLBuilder {
    private val builder = StringBuilder()

    fun html(content: PrettyPrintHTMLBuilder.() -> Unit) {
        builder.appendLine("<html>")
        this.content()
        builder.appendLine("</html>")
    }

    fun head(content: PrettyPrintHTMLBuilder.() -> Unit) {
        builder.appendLine("<head>")
        this.content()
        builder.appendLine("</head>")
    }

    fun title(content: String) {
        builder.appendLine("<title>$content</title>")
    }

    fun body(content: PrettyPrintHTMLBuilder.() -> Unit) {
        builder.appendLine("<body>")
        this.content()
        builder.appendLine("</body>")
    }

    fun h1(content: String) {
        builder.appendLine("<h1>$content</h1>")
    }

    fun h2(content: String) {
        builder.appendLine("<h2>$content</h2>")
    }

    fun p(content: PrettyPrintHTMLBuilder.() -> Unit) {
        builder.appendLine("<p>")
        this.content()
        builder.appendLine("</p>")
    }

    fun a(href: String, content: String) {
        builder.appendLine("""<a href="$href">$content</a>""")
    }

    fun br() {
        builder.appendLine("<br/>")
    }

    operator fun String.unaryPlus() {
        builder.appendLine(this)
    }

    override fun toString() = builder.toString()
}

fun prettyPrintHTML(block: PrettyPrintHTMLBuilder.() -> Unit): String {
    val builder = PrettyPrintHTMLBuilder()
    builder.block()
    return builder.toString()
}
