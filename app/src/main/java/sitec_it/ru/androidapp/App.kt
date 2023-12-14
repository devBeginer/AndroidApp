package sitec_it.ru.androidapp

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import org.acra.ACRA
import org.acra.BuildConfig
import org.acra.config.mailSender
import org.acra.config.toast
import org.acra.data.StringFormat
import org.acra.ktx.initAcra


@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        initAcra {
            //core configuration:
            buildConfigClass = BuildConfig::class.java
            reportFormat = StringFormat.JSON
            //each plugin you chose above can be configured in a block like this:
            toast {
                text = getString(R.string.crash_toast_text)
            }

            mailSender {
                mailTo = "sfr@sitec-it.ru"
                reportFileName = "Crash.txt"
            }
        }
    }
}