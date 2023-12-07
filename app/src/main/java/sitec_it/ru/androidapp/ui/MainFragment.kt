package sitec_it.ru.androidapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.Utils.observeFutureEvents
import sitec_it.ru.androidapp.data.models.User
import sitec_it.ru.androidapp.ui.settings.SettingsContainerFragment
import sitec_it.ru.androidapp.viewModels.MainViewModel

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonLogin = view.findViewById<Button>(R.id.btn_sign_in)
        val editTextLogin = view.findViewById<EditText>(R.id.et_login)
        val editTextPassword = view.findViewById<EditText>(R.id.et_password)
        val btnSettings = view.findViewById<Button>(R.id.btn_sign_in_settings)
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar_main)
        viewModel.prepopulateUser()
        viewModel.login.observeFutureEvents(viewLifecycleOwner, Observer {login->
            editTextLogin.setText(login)
        })

        viewModel.initLoginField()


        /*viewModel.user.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                val snackbar: Snackbar = Snackbar.make(
                    requireView(),
                    it, Snackbar.LENGTH_LONG
                )
                val view = snackbar.view
                val txtv = view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
                txtv.maxLines = 5
                snackbar.show()
                //Toast.makeText(context, "$it", Toast.LENGTH_LONG).show()
                //Toast.makeText(context, "Пользователь: ${it.name}", Toast.LENGTH_LONG).show()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.nav_host_fragment, MenuFragment())
                    ?.commit()
            }else{
                Toast.makeText(context, "Неверный логин или пароль", Toast.LENGTH_LONG).show()
            }
        })*/
        viewModel.apiResult.observe(viewLifecycleOwner, object : Observer<String?> {
            var isFirst = true

            override fun onChanged(value: String?) {
                if (isFirst) {
                    isFirst = false
                } else {
                    if (value != null) {
                        val snackbar: Snackbar = Snackbar.make(
                            requireView(),
                            value, Snackbar.LENGTH_LONG
                        )
                        val view = snackbar.view
                        val txtv =
                            view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
                        txtv.maxLines = 5
                        snackbar.show()
                    } else {
                        Toast.makeText(context, "Ошибка api запроса", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        })
        viewModel.user.observeFutureEvents(viewLifecycleOwner, Observer<User?> {user->

            if (user != null) {

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.nav_host_fragment, MenuFragment())
                    ?.commit()
            } else {
                Toast.makeText(context, "Неверный логин", Toast.LENGTH_LONG)
                    .show()
            }


        })
        /*viewModel.user.observeFutureEvents(viewLifecycleOwner, Observer {
            if(it!=null){
                val snackbar: Snackbar = Snackbar.make(
                    requireView(),
                    it, Snackbar.LENGTH_LONG
                )
                val view = snackbar.view
                val txtv = view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
                txtv.maxLines = 5
                snackbar.show()

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.nav_host_fragment, MenuFragment())
                    ?.addToBackStack(null)
                    ?.commit()
            }else{
                Toast.makeText(context, "Неверный логин или пароль", Toast.LENGTH_LONG).show()
            }
        })*/

        buttonLogin.setOnClickListener {
            viewModel.login(editTextLogin.text.toString(), editTextPassword.text.toString())
            /*if(editTextLogin.text.isNotEmpty() && editTextPassword.text.isNotEmpty()){
                viewModel.login(editTextLogin.text.toString(), editTextPassword.text.toString())
            }else{
                Toast.makeText(context, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            }*/
        }
        editTextPassword.setOnEditorActionListener { textView, id, keyEvent ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                viewModel.login(editTextLogin.text.toString(), editTextPassword.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        editTextLogin.setOnEditorActionListener { textView, id, keyEvent ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                viewModel.login(editTextLogin.text.toString(), editTextPassword.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        btnSettings.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.nav_host_fragment, SettingsContainerFragment())
                //?.addToBackStack(null)
                ?.commit()
        }
        /*toolbar.inflateMenu(R.menu.menu_toolbar_main_fragment)
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.item_settings) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.nav_host_fragment, SettingsContainerFragment())
                    ?.addToBackStack(null)
                    ?.commit()
            }

            return@setOnMenuItemClickListener false
        }*/

    }
}