package sitec_it.ru.androidapp.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.ui.LoginFragment

class AdditionSettingsFragment: Fragment(R.layout.fragment_addition_settings) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val back = view.findViewById<ImageView>(R.id.iv_addition_settings_back)
        back.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.nav_host_fragment, LoginFragment())?.commit()
        }

    }
}