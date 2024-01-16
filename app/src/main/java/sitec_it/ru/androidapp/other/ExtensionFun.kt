package sitec_it.ru.androidapp.other

import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar


fun View.onClickDebounce(debounceDuration: Long = 900L, action: (View) -> Unit) =
    setOnClickListener(DebouncedOnClickListener(debounceDuration, action))


private class DebouncedOnClickListener(
    private val debounceDuration: Long,
    private val clickAction: (View) -> Unit
) : View.OnClickListener {
    private var lastClickTime: Long = 0L
    override fun onClick(v: View) {
        val now = System.currentTimeMillis()
        if ((now - lastClickTime) > debounceDuration) {
            lastClickTime = now
            clickAction(v)
        }
    }
}

fun Fragment?.runOnUiThread(action: () -> Unit) {
    this ?: return
    if (!isAdded) return // Fragment not attached to an Activity
    activity?.runOnUiThread(action)
}
fun Fragment?.showSnack(view: View,text:String,maxLine:Int){
    val snack: Snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG)
    val tv = snack.view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
    tv.maxLines = maxLine
    snack.show()
}