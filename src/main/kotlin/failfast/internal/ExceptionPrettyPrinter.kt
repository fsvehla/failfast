package failfast.internal

class ExceptionPrettyPrinter(val throwable: Throwable) {
    fun prettyPrint(): String {
        val stackTrace =
            throwable.stackTrace
                .filter { it.lineNumber > 0 }
                .dropLastWhile { !it.className.startsWith("failfast") }
        return "${throwable.javaClass.name} : ${throwable.message}\n\tat " +
                stackTrace.joinToString("\n\tat ")
    }
}
