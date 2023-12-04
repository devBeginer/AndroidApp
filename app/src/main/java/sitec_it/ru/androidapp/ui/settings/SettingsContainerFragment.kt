package sitec_it.ru.androidapp.ui.settings

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import sitec_it.ru.androidapp.R

class SettingsContainerFragment: Fragment(R.layout.fragment_settings_container) {
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