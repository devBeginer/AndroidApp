package sitec_it.ru.androidapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.viewModels.MainViewModel

@AndroidEntryPoint
class MainFragment: Fragment() {

    private val viewModel:MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.prepopulate()
        viewModel.user.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                Toast.makeText(context, "Пользователь: ${it.name}", Toast.LENGTH_LONG).show()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.nav_host_fragment, MenuFragment())
                    ?.commit()
            }else{
                Toast.makeText(context, "Неверный логин или пароль", Toast.LENGTH_LONG).show()
            }
        })

        val buttonLogin = view.findViewById<Button>(R.id.btn_sign_in)
        val editTextLogin = view.findViewById<EditText>(R.id.et_login)
        val editTextPassword = view.findViewById<EditText>(R.id.et_password)
        buttonLogin.setOnClickListener {
            if(editTextLogin.text.isNotEmpty() && editTextPassword.text.isNotEmpty()){
                viewModel.login(editTextLogin.text.toString(), editTextPassword.text.toString())
            }else{
                Toast.makeText(context, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            }
        }
    }
}