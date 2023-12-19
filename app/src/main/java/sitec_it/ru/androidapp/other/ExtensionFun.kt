package sitec_it.ru.androidapp.other

import android.view.View


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