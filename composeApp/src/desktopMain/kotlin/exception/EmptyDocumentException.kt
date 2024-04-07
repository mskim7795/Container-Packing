package exception

import java.io.Serial

class EmptyDocumentException: Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)

    companion object {
        @Serial
        private const val serialVersionUID: Long = 2267369128993080865L
    }
}