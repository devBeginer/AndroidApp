package sitec_it.ru.androidapp

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.net.UnknownHostException

object Utils {
    fun <T> LiveData<T>.observeFutureEvents(owner: LifecycleOwner, observer: Observer<T>) {
        observe(owner, object : Observer<T> {
            var isFirst = true

            override fun onChanged(value: T) {
                if (isFirst) {
                    isFirst = false
                } else {
                    observer.onChanged(value)
                }
            }
        })
    }


    fun hostIsReachable(host: String): Boolean {
        var result = false
        result = try {
            val runtime = Runtime.getRuntime()
            val proc = runtime.exec("ping -c 1 $host")
            val mPingResult = proc.waitFor()
            mPingResult == 0
        } catch (e: InterruptedException) {
            Log.d("CHECK_CONNECTION", "InterruptedException $e")
            false
        }catch (e: UnknownHostException) {
            Log.d("CHECK_CONNECTION", "Unknown host exception")
            false
        } catch (e: IOException) {
            Log.d("CHECK_CONNECTION", "Host unreachable")
            false
        }
        return result
    }

    fun showSnackBar(view:View, text: String, maxLines: Int){
        val snackbar: Snackbar = Snackbar.make(
            view,
            text, Snackbar.LENGTH_LONG
        )
        val snackbarView = snackbar.view
        val txtv =
            snackbarView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        txtv.maxLines = maxLines
        snackbar.show()
    }

    fun showShortToast(context: Context, text: String){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(context: Context, text: String){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}
enum class TypeForms{
    MENU,SUBMENU,GENERALFORM
}