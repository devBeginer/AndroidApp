package sitec_it.ru.androidapp.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import retrofit2.Response
import java.io.IOException

class NetworkHelper constructor(private val context: Context) {

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

    suspend fun <T> safeApiCall(
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
    }

}