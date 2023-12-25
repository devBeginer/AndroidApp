package sitec_it.ru.androidapp.network

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(
        val code: Int,
        val errorMessage: String,
        val additionalDescription: String,
        val response: String? = null,
        val body: String? = null,
        val exception: Exception? = null
    ) : Result<Nothing>() {
        var description: String = when (code) {
            0 -> additionalDescription
            200 -> additionalDescription
            201 -> additionalDescription
            202 -> "ERROR - Server error"
            404 -> "ERROR - Not found"
            403 -> "ERROR - Forbidden"
            401 -> "ERROR - Unauthorized"
            400 -> "ERROR - Bad request"
            500 -> "ERROR - Internal Server Error"
            else -> "ERROR - Unknown error"
        }

        override fun toString(): String {
            return "$errorMessage, $description ($code)"
        }

        fun errorStringFormat(): String {
            return "$errorMessage, $description ($code)"
        }

        fun errorStringFormatLong(): String {
            return "errorMessage = $errorMessage, " +
                    "description = $description, " +
                    "code = $code, " +
                    "additionalDescription = $additionalDescription, " +
                    "response = $response, " +
                    "body = $body, " +
                    "exception = ${exception?.message}"
        }
    }
}
