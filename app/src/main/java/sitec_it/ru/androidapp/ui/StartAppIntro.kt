package sitec_it.ru.androidapp.ui

import android.Manifest
import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.github.appintro.AppIntroFragment
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroPageTransformerType
import dagger.hilt.android.AndroidEntryPoint
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.SharedPreferencesUtils.editPref
import javax.inject.Inject
@AndroidEntryPoint
class StartAppIntro : AppIntro2() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences.editPref("firstStartApp", false)
        // Make sure you don't call setContentView!

        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment

        addSlide(
            AppIntroFragment.createInstance(
            title = "Добро пожаловать...",
            description = "Изучите основные возможности приложения",
            imageDrawable = R.drawable.screen_start_fragment,
            backgroundColorRes = R.color.app_intro_background,
            titleColorRes = R.color.black,
            descriptionColorRes = R.color.black
        ))
        addSlide(AppIntroFragment.createInstance(
            title = "Настройки подключения",
            description = "Заполните все поля или отсканируйте QR код с настройками и переходите к следующему шагу",
            imageDrawable = R.drawable.screen_base_fragment,
            //backgroundDrawable = R.drawable.screen_base_fragment,
            //titleColorRes = R.color.yellow,
            //descriptionColorRes = R.color.red,
            backgroundColorRes = R.color.app_intro_background,
            titleColorRes = R.color.black,
            descriptionColorRes = R.color.black
            //titleTypefaceFontRes = R.font.opensans_regular,
            //descriptionTypefaceFontRes = R.font.opensans_regular,
        ))
        addSlide(AppIntroFragment.createInstance(
            title = "Настройки лицензирования",
            description = "Заполните все поля или отсканируйте QR код с настройками и переходите к следующему шагу",
            imageDrawable = R.drawable.screen_license_fragment,
            backgroundColorRes = R.color.app_intro_background,
            titleColorRes = R.color.black,
            descriptionColorRes = R.color.black
        ))
        addSlide(AppIntroFragment.createInstance(
            title = "На этом все...",
            description = "Переходим к работе",

            backgroundColorRes = R.color.app_intro_background,
            titleColorRes = R.color.black,
            descriptionColorRes = R.color.black
        ))

        ContextCompat.getDrawable(this, R.drawable.custom_icon_appintro_close_button)
            ?.let { setImageSkipButton(it) }
        /*ContextCompat.getDrawable(this, R.drawable.custom_icon_appintro_next_button)
            ?.let { setImageDoneButton(it) }*/
        //setSkipArrowColor(Color.WHITE)

        //setTransformer(AppIntroPageTransformerType.Zoom)
        isIndicatorEnabled = true
        setImmersiveMode()
        isSystemBackButtonLocked = true
        askForPermissions(
            permissions = arrayOf(Manifest.permission.CAMERA),
            slideNumber = 2,
            required = true)

    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        finish()
    }
}