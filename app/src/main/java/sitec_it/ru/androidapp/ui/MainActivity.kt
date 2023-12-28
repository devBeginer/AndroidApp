package sitec_it.ru.androidapp.ui

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.Observer
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.material.progressindicator.CircularProgressIndicator
import dagger.hilt.android.AndroidEntryPoint
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.repository.LocalRepository
import sitec_it.ru.androidapp.viewModels.SharedViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: SharedViewModel by viewModels()

    //lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //var progressBar: ProgressBar = findViewById(R.id.pb_main)
        var progressBarGroup: Group = findViewById(R.id.pb_group)
        var pbCircular: CircularProgressIndicator = findViewById(R.id.pb_circular)
        var pbLinear: LinearLayout = findViewById(R.id.pb_main_linear)
        //var fadeTransition: Transition = Fade()
        //fadeTransition.addTarget(pbLinear)
        val isFirstStartApp = viewModel.isFirstStartApp()



        viewModel.pbVisibility.observe(this, Observer { visibility->
            if(visibility) {
                //progressBar.visibility = View.VISIBLE
                //progressBarGroup.visibility = View.VISIBLE

                val mFade: Fade = Fade(Fade.IN)
                TransitionManager.beginDelayedTransition(pbLinear, mFade)
                pbLinear.visibility = View.VISIBLE
                //pbCircular.show()

            }
            else {
                //progressBar.visibility = View.GONE
                //progressBarGroup.visibility = View.GONE

                //pbCircular.hide()
                //pbCircular.setVisibilityAfterHide(View.GONE)
                val mFade: Fade = Fade(Fade.OUT)
                TransitionManager.beginDelayedTransition(pbLinear, mFade)
                pbLinear.visibility = View.GONE
            }

        })

        if(isFirstStartApp){
            val intent = Intent(this, StartAppIntro::class.java)
            startActivity(intent)
        } /*else {

        }*/




        if(!isFirstStartApp) {
            viewModel.initData()
            viewModel.profileList.observe(this, Observer { count->
                if(count>0)
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, LoginFragment())
                        .commit()
                else
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, StartFragment())
                        .commit()
            })
           /* supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, LoginFragment())
                .commit()*/
        }
        else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, StartFragment())
                .commit()
        }




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