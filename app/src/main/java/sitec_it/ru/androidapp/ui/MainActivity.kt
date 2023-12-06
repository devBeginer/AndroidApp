package sitec_it.ru.androidapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.viewModels.MainViewModel
import sitec_it.ru.androidapp.viewModels.SharedViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.initData()
        viewModel.profileList.observe(this, Observer { count->
            if(count>0)
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, MainFragment())
                    .commit()
            else
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, StartFragment())
                    .commit()
        })

    }
}