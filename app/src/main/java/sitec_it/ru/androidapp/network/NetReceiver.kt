package sitec_it.ru.androidapp.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class NetReceiver: BroadcastReceiver() {
    companion object{
        var CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE"
    }
    override fun onReceive(context: Context, intent: Intent) {
        val actionOfIntent: String? = intent.action
        if (actionOfIntent == CONNECTIVITY_ACTION) {
            checkConnection(context)
        }
    }

    private fun checkConnection(context: Context) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}