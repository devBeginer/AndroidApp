package sitec_it.ru.androidapp.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.data.models.DialogParams
import sitec_it.ru.androidapp.ui.LoginFragment
import sitec_it.ru.androidapp.viewModels.SharedViewModel
@AndroidEntryPoint
class AdditionSettingsFragment: Fragment(R.layout.fragment_addition_settings) {

    private val sharedViewModel: SharedViewModel by viewModels({requireActivity()})
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val back = view.findViewById<ImageView>(R.id.iv_addition_settings_back)
        back.setOnClickListener {
            /*activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.nav_host_fragment, LoginFragment())?.commit()*/
            processBack()
        }

    }
    private fun processBack(){
        if(sharedViewModel.databaseId != ""){
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.nav_host_fragment, LoginFragment())?.commit()
        }else{
            sharedViewModel.postDialog(
                DialogParams(
                    "Для продолжения необходимо создать узел подключения",
                    "Внимание",
                    "Ok"
                )
            )
        }
    }
}