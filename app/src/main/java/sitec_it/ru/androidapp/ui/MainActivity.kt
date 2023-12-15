package sitec_it.ru.androidapp.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.viewModels.SharedViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: SharedViewModel by viewModels()

    //lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var progressBar: ProgressBar = findViewById(R.id.pb_main)

        viewModel.pbVisibility.observe(this, Observer { visibility->
            if(visibility)
                progressBar.visibility = View.VISIBLE
            else
                progressBar.visibility = View.GONE
        })


        viewModel.initData()
        viewModel.profileList.observe(this, Observer { count->
            if(count>0)
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, LoginFragment())
                    .commit()
            else
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, StartFragment())
                    .commit()
        })




    }

    private fun showApplicationDialog(
        textMessage: String,
        title: String,
        positiveBtn: String,
        onPositive:()->Unit,
        negativeButton: String?,
        onNegative: (() -> Unit)?,
        neutralButton: String?,
        onNeutral: (() -> Unit)?
    ){


        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setCancelable(false)
        alertDialog.setTitle(title)
        alertDialog.setMessage(textMessage)
        alertDialog.setPositiveButton(positiveBtn) { dialog: DialogInterface, which: Int ->
            onPositive()
        }
        if(negativeButton!=null && onNegative!=null){
            alertDialog.setNegativeButton(negativeButton) { dialog: DialogInterface?, which: Int ->
                onNegative()
            }
        }

        if(neutralButton!=null && onNeutral!=null){
            alertDialog.setNeutralButton(neutralButton) { dialog: DialogInterface?, which: Int ->
                onNeutral()
            }
        }


        alertDialog.show()
    }

}