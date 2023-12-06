package sitec_it.ru.androidapp

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

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
}