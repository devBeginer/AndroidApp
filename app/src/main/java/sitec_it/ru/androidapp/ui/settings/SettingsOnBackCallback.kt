package sitec_it.ru.androidapp.ui.settings

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.data.models.DialogParams
import sitec_it.ru.androidapp.ui.LoginFragment
import sitec_it.ru.androidapp.viewModels.SharedViewModel

class SettingsOnBackCallback {

    fun settingsOnBack(sharedViewModel: SharedViewModel, activity: FragmentActivity){
        if (sharedViewModel.databaseId!=""){
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.nav_host_fragment, LoginFragment())?.commit()
        } else{
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