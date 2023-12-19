package sitec_it.ru.androidapp.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import sitec_it.ru.androidapp.BuildConfig
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.Utils.observeFutureEvents
import sitec_it.ru.androidapp.data.models.profile.Profile
import sitec_it.ru.androidapp.data.models.user.User
import sitec_it.ru.androidapp.data.models.user.UserSpinner
import sitec_it.ru.androidapp.ui.settings.NothingSelectedSpinnerAdapter
import sitec_it.ru.androidapp.ui.settings.SettingsContainerFragment
import sitec_it.ru.androidapp.viewModels.LoginViewModel
import sitec_it.ru.androidapp.viewModels.SharedViewModel

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels({ requireActivity() })
    private var userList = arrayListOf<UserSpinner>()
    private lateinit var arrayAdapter: ArrayAdapter<UserSpinner>
    lateinit var tvVersion:TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonLogin = view.findViewById<Button>(R.id.btn_sign_in)
        //val editTextLogin = view.findViewById<EditText>(R.id.et_login)
        val editTextPassword = view.findViewById<EditText>(R.id.et_password)
        val btnSettings = view.findViewById<Button>(R.id.btn_sign_in_settings)
        val tvSignName = view.findViewById<AutoCompleteTextView>(R.id.tv_SignName)
        val tvProfile = view.findViewById<TextView>(R.id.tv_sign_in)
        tvVersion = view.findViewById(R.id.tv_version)
        initView()

        /*viewModel.login.observeFutureEvents(viewLifecycleOwner, Observer { code ->

            val index = userList.indexOfFirst { userSpinner -> userSpinner.code == code }
            if(index!=-1) spinner.setSelection(index)
        })*/



        //spinner.adapter = arrayAdapter
        val array = NothingSelectedSpinnerAdapter(arrayAdapter, requireContext(), R.layout.spinner_row_nothing_selected)
        viewModel.userList.observe(viewLifecycleOwner, Observer {
            it?.let{ list ->
                val listNamesUsers = mutableListOf<String>()
                list.forEach { item ->
                    listNamesUsers.add(item.name)
                }
                val arrayAdapter =
                    ArrayAdapter(requireContext(), R.layout.dropdown_item, listNamesUsers.sortedWith(String.CASE_INSENSITIVE_ORDER))
                tvSignName.setAdapter(arrayAdapter)
                if (listNamesUsers.size > 0) {
                    tvSignName.setText(listNamesUsers[0], false)
                    tvSignName.setSelection(tvSignName.text.count())
                }
                tvSignName.setOnItemClickListener { adapterView, view, i, l ->
                    val user = adapterView.adapter.getItem(i).toString()
                    val codeUser = list.find { it.name.equals(user) }?.code
                    viewModel.saveUserToSp(codeUser)
                    sharedViewModel.currentUserName = user
                }
                sharedViewModel.updateProgressBar(false)
            }
        })


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
        viewModel.apiError.observe(viewLifecycleOwner, object : Observer<String?> {
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
        viewModel.user.observeFutureEvents(viewLifecycleOwner, Observer<User?> { user ->

            if (user != null) {

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.nav_host_fragment, MenuFragment())
                    ?.commit()
            } else {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, MenuFragment())
                    .commit()
                Toast.makeText(context, "Неверный логин", Toast.LENGTH_LONG)
                    .show()
            }


        })
        viewModel.profile.observeFutureEvents(viewLifecycleOwner, Observer<Profile> { profile ->

            tvProfile.text = "${tvProfile.text}\n ${profile.name}"

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
            //viewModel.login(editTextLogin.text.toString(), editTextPassword.text.toString())
            viewModel.login(tvSignName.text.toString(), editTextPassword.text.toString())
            /*if(editTextLogin.text.isNotEmpty() && editTextPassword.text.isNotEmpty()){
                viewModel.login(editTextLogin.text.toString(), editTextPassword.text.toString())
            }else{
                Toast.makeText(context, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            }*/
        }
     /*   editTextPassword.setOnEditorActionListener { textView, id, keyEvent ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                //viewModel.login(editTextLogin.text.toString(), editTextPassword.text.toString())
                viewModel.login((spinner.selectedItem as UserSpinner).login, editTextPassword.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }*/

        /*editTextLogin.setOnEditorActionListener { textView, id, keyEvent ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                viewModel.login(editTextLogin.text.toString(), editTextPassword.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }*/

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

    private fun initView() {
        tvVersion.apply {
            text = "v ${BuildConfig.VERSION_NAME}"
            setTextColor(Color.BLACK)
            textSize = 15F
        }
        arrayAdapter = ArrayAdapter<UserSpinner>(
            requireContext(),
            //R.layout.custom_spinner_item,
            android.R.layout.simple_spinner_item,
            userList
        )
        arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_drop_down_item)

        sharedViewModel.updateProgressBar(true)
        viewModel.loadUsers()
        //viewModel.initLoginField()
        viewModel.initProfileName()
    }
}