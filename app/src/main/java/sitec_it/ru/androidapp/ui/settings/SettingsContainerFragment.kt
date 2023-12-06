package sitec_it.ru.androidapp.ui.settings

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.ui.MainFragment

class SettingsContainerFragment: Fragment(R.layout.fragment_settings_container) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This callback is only called when MyFragment is at least started
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.nav_host_fragment, MainFragment())?.commit()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView: BottomNavigationView = view.findViewById(R.id.settings_bottom_navigation_view)
        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            var selectedFragment = when (item.itemId) {
                R.id.baseFragment -> BaseSettingsFragment()
                R.id.licenseFragment -> LicenseSettingsFragment()
                R.id.additionFragment -> AdditionSettingsFragment()
                else -> null
            }
            if (selectedFragment != null) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.settings_nav_host_fragment, selectedFragment)?.commit()
            }

            true
        }


        if (savedInstanceState == null) activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.settings_nav_host_fragment, BaseSettingsFragment())?.commit()


    }
}