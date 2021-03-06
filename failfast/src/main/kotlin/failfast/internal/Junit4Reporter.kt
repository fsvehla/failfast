package failfast.internal

import failfast.Failed
import failfast.Ignored
import failfast.Success
import failfast.TestResult

// based on a snippet by Ben Woodworth on the kotlin slack
fun String.xmlEscape(): String = buildString(length + 30) {
    for (char in this@xmlEscape) {
        when (char) {
            '\n' -> append("&#13;&#10;")
            '&' -> append("&amp;")
            '<' -> append("&lt;")
            '>' -> append("&gt;")
            '"' -> append("&quot;")
            '\'' -> append("&apos;")
            else -> append(char)
        }
    }
}

class Junit4Reporter(private val testResults: List<TestResult>) {
    fun stringReport(): List<String> {
        val result = mutableListOf("<testsuite tests=\"${testResults.size}\">")
        testResults.forEach {

            val line = when (it) {
                is Success ->
                    listOf("""<testcase classname="${it.test.parentContext.stringPath()}" name="${it.test.testName}"/>""")
                is Failed -> {
                    listOf(
                        """<testcase classname="${it.test.parentContext.stringPath()}" name="${it.test.testName}">""",
                        """<failure message="${it.failure.message?.xmlEscape()}">""",
                        ExceptionPrettyPrinter(it.failure).stackTrace.joinToString("\n"),
                        """</failure>""",
                        """</testcase>"""
                    )
                }
                is Ignored -> {
                    listOf(
                        """<testcase classname="${it.test.parentContext.stringPath()}" name="${it.test.testName}">""",
                        """<skipped/></testcase>"""
                    )
                }
            }
            result.addAll(line)
        }
        result.add("</testsuite>")
        return result
    }

}
