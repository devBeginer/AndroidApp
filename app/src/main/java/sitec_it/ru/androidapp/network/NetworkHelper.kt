package sitec_it.ru.androidapp.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import retrofit2.Response
import java.io.IOException

class NetworkHelper(private val context: Context) {

    fun isNetworkConnected(): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

    /*suspend fun <T> safeApiCall(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): T? {
        val result: Result<T> =
            safeApiResult(call, errorMessage)
        var data: T? = null

        when (result) {
            is Result.Success -> data = result.data
            is Result.Error -> {
                Log.d("safeApiCall", "${result.exception}")
            }
        }

        return data
    }

    private suspend fun <T> safeApiResult(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): Result<T> {
        if(!isNetworkConnected()) return Result.Error(IOException("$errorMessage, ERROR - Connection error"))
        val response = call.invoke()
        return if (response.isSuccessful)
            response.body()?.let { body->Result.Success(body) }
                ?: Result.Error(IOException("$errorMessage, ERROR - Empty result (${response.code()})"))
        else
            Result.Error(IOException("$errorMessage, ERROR - Api call error (${response.code()})"))
    }*/

    suspend fun <T> safeApiCall(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): Result<T> {
        /*val result: Result<T> = if(!isNetworkConnected()){
            Result.Error(IOException("$errorMessage, ERROR - Connection error"))
        }else{
            try {
                val response = call.invoke()

                when(response.code()){
                    200 -> {
                        response.body()?.let { body -> Result.Success(body) } ?: Result.Error(
                            IOException("$errorMessage, ERROR - Empty result (${response.code()})")
                        )
                    }
                    201 -> {
                        response.body()?.let { body -> Result.Success(body) } ?: Result.Error(
                            IOException("$errorMessage, ERROR - Empty result (${response.code()})")
                        )
                    }
                    202 -> {
                        response.body()?.let { body -> Result.Success(body) } ?: Result.Error(
                            IOException("$errorMessage, ERROR - Empty result (${response.code()})")
                        )
                    }
                    404 -> Result.Error(IOException("$errorMessage, ERROR - Not found (${response.code()})"))
                    403 -> Result.Error(IOException("$errorMessage, ERROR - Forbidden (${response.code()})"))
                    401 -> Result.Error(IOException("$errorMessage, ERROR - Unauthorized (${response.code()})"))
                    400 -> Result.Error(IOException("$errorMessage, ERROR - Bad request (${response.code()})"))
                    500 -> Result.Error(IOException("$errorMessage, ERROR - Internal Server Error (${response.code()})"))
                    else -> Result.Error(IOException("$errorMessage, ERROR - Unknown error (${response.code()})"))

                }
            }catch (e: Exception){
                Result.Error(IOException("$errorMessage, ERROR - Connection"))
            }

        }*/
        val result: Result<T> = if (!isNetworkConnected()) {
            Result.Error(0, errorMessage, "ERROR - Connection error")
        } else {
            try {
                val response = call.invoke()

                when (response.code()) {
                    200 -> {
                        response.errorBody()
                        response.body()?.let { body -> Result.Success(body) } ?: Result.Error(
                            response.code(),
                            errorMessage,
                            "ERROR - Empty result"
                        )
                    }

                    201 -> {
                        response.body()?.let { body -> Result.Success(body) } ?: Result.Error(
                            response.code(),
                            errorMessage,
                            "ERROR - Empty result"
                        )
                    }
                    /*  202 -> {
                          response.body()?.let { body -> Result.Success(body) } ?: Result.Error(response.code(), errorMessage, "ERROR - Empty result")
                      }*/
                    else -> response.errorBody()
                        ?.let { error ->
                            Result.Error(
                                response.code(),
                                errorMessage,
                                "",
                                Exception(error.string())
                            )
                        }
                        ?: Result.Error(response.code(), errorMessage, "")

                }
            } catch (e: Exception) {
                Result.Error(0, errorMessage, "ERROR - Connection error", e)
            }

        }

        if (result is Result.Error) Log.d("safeApiCall", "${result.errorStringFormat()}")


        return result
    }

    /*suspend fun <T> safeApiCall(
            call: suspend () -> Response<T>,
            errorMessage: String
        ): T? {
            val result: Result<T> = if(!isNetworkConnected()){
                Result.Error(IOException("$errorMessage, ERROR - Connection error"))
            }else{
                try {
                    val response = call.invoke()

                    when(response.code()){
                        200 -> {
                            response.body()?.let { body -> Result.Success(body) } ?: Result.Error(
                                IOException("$errorMessage, ERROR - Empty result (${response.code()})")
                            )
                        }
                        201 -> {
                            response.body()?.let { body -> Result.Success(body) } ?: Result.Error(
                                IOException("$errorMessage, ERROR - Empty result (${response.code()})")
                            )
                        }
                        202 -> {
                            response.body()?.let { body -> Result.Success(body) } ?: Result.Error(
                                IOException("$errorMessage, ERROR - Empty result (${response.code()})")
                            )
                        }
                        404 -> Result.Error(IOException("$errorMessage, ERROR - Not found (${response.code()})"))
                        403 -> Result.Error(IOException("$errorMessage, ERROR - Forbidden (${response.code()})"))
                        401 -> Result.Error(IOException("$errorMessage, ERROR - Unauthorized (${response.code()})"))
                        400 -> Result.Error(IOException("$errorMessage, ERROR - Bad request (${response.code()})"))
                        500 -> Result.Error(IOException("$errorMessage, ERROR - Internal Server Error (${response.code()})"))
                        else -> Result.Error(IOException("$errorMessage, ERROR - Unknown error (${response.code()})"))

                    }
                }catch (e: Exception){
                    Result.Error(IOException("$errorMessage, ERROR - Connection"))
                }

            }

            var data: T? = when (result) {
                is Result.Success -> result.data
                is Result.Error -> {
                    Log.d("safeApiCall", "${result.exception}")
                    null
                }
            }

            return data
        }*/

}