package failfast

import strikt.api.expectThat
import strikt.assertions.isEqualTo

object RootContextTest {
    val context: RootContext = describe(RootContext::class) {
        val ste = Throwable().stackTrace.first()
        val testContext = RootContext {}
        it("knows its file and line number") {
            expectThat(testContext.stackTraceElement) {
                get { lineNumber }.isEqualTo(ste.lineNumber + 1)
                get { className }.isEqualTo(ste.className)
            }
        }
    }
}
