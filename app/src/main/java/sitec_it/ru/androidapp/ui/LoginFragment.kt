package sitec_it.ru.androidapp.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.Utils.observeFutureEvents
import sitec_it.ru.androidapp.data.models.profile.Profile
import sitec_it.ru.androidapp.data.models.user.User
import sitec_it.ru.androidapp.data.models.user.UserSpinner
import sitec_it.ru.androidapp.ui.settings.NothingSelectedSpinnerAdapter
import sitec_it.ru.androidapp.ui.settings.SettingsContainerFragment
import sitec_it.ru.androidapp.viewModels.LoginViewModel
import sitec_it.ru.androidapp.viewModels.SharedViewModel

import sitec_it.ru.androidapp.BuildConfig

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels({ requireActivity() })
    private var userList = arrayListOf<UserSpinner>()
    private lateinit var arrayAdapter: ArrayAdapter<UserSpinner>
    lateinit var tvVersion: TextView
    private var isScannerMode: Boolean = false
    private lateinit var codeScanner: CodeScanner
    private lateinit var flScanner: FrameLayout
    private lateinit var scrollView: ScrollView
    private lateinit var llToolbar: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (isScannerMode) {
                flScanner.visibility = View.GONE
                llToolbar.visibility = View.VISIBLE
                scrollView.visibility = View.VISIBLE
                isScannerMode = false
                codeScanner.releaseResources()
            }
        }
    }

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
        //val buttonLoginQR = view.findViewById<Button>(R.id.btn_sign_in_qr)
        val buttonLoginQR = view.findViewById<ImageView>(R.id.iv_sign_in_qr)
        //val editTextLogin = view.findViewById<EditText>(R.id.et_login)
        val editTextPassword = view.findViewById<EditText>(R.id.et_password)
        val btnSettings = view.findViewById<ImageView>(R.id.iv_sign_in_settings)
        val tvSignName = view.findViewById<AutoCompleteTextView>(R.id.tv_SignName)
        val tvProfile = view.findViewById<TextView>(R.id.tv_sign_in)
        flScanner = view.findViewById<FrameLayout>(R.id.frameLayout_login_scan_qr)
        scrollView = view.findViewById<ScrollView>(R.id.scrollView_login)
        llToolbar = view.findViewById<ConstraintLayout>(R.id.cl_login_toolbar)
        tvVersion = view.findViewById(R.id.tv_version)
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        codeScanner = CodeScanner(requireActivity(), scannerView)
        initView()
        var selectedLogin: String? = ""

        /*viewModel.login.observeFutureEvents(viewLifecycleOwner, Observer { code ->

            val index = userList.indexOfFirst { userSpinner -> userSpinner.code == code }
            if(index!=-1) spinner.setSelection(index)
        })*/


        //spinner.adapter = arrayAdapter
        val array = NothingSelectedSpinnerAdapter(
            arrayAdapter,
            requireContext(),
            R.layout.spinner_row_nothing_selected
        )
        viewModel.userList.observe(viewLifecycleOwner, Observer {
            it?.let { list ->
                val listNamesUsers = mutableListOf<String>()
                list.forEach { item ->
                    listNamesUsers.add(item.name)
                }
                val arrayAdapter =
                    ArrayAdapter(
                        requireContext(),
                        R.layout.dropdown_item,
                        listNamesUsers.sortedWith(String.CASE_INSENSITIVE_ORDER)
                    )
                tvSignName.setAdapter(arrayAdapter)

                tvSignName.doAfterTextChanged { userName ->
                    val user = list.find { it.name == userName.toString() }
                    val codeUser = user?.code
                    selectedLogin = user?.login
                    viewModel.saveUserToSp(codeUser)
                    sharedViewModel.currentUserName = userName.toString()
                }

                if (listNamesUsers.size > 0) {
                    tvSignName.setText(listNamesUsers[0], false)
                    tvSignName.setSelection(0/*tvSignName.text.count()*/)
                }
                tvSignName.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (parent != null) {
                            val user = parent.adapter.getItem(position).toString()
                            val codeUser = list.find { it.name == user }?.code
                            selectedLogin = list.find { it.name == user }?.login
                            viewModel.saveUserToSp(codeUser)
                            sharedViewModel.currentUserName = user
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }

                tvSignName.setOnItemClickListener { adapterView, view, i, l ->
                    val user = adapterView.adapter.getItem(i).toString()
                    val codeUser = list.find { it.name == user }?.code
                    selectedLogin = list.find { it.name == user }?.login
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
        viewModel.apiError.observe(viewLifecycleOwner, Observer {
            it?.let { error ->
                when (error) {
                    "errorAuth" -> {
                        Snackbar.make(
                            requireView(),
                            "Ошибка авторизации.Проверьте пароль и повторите еще раз.",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    else -> {
                        sharedViewModel.updateProgressBar(false)
                        Snackbar.make(
                            requireView(),
                            "Ошибка подключения к базе. Проверьте данные в настройках и попробуйте еще раз.",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }

            }
        })
    /*    viewModel.user.observeFutureEvents(viewLifecycleOwner, Observer<User?> { user ->

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


        })*/
        viewModel.responseGetLogin.observe(viewLifecycleOwner, Observer {
            it?.let { response ->
                //sharedViewModel.menuForms.postValue(response)
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.nav_host_fragment, MenuFragment())
                    ?.commit()
            }
        })

        viewModel.authenticationUser.observe(viewLifecycleOwner, Observer<String> { result ->

            when (result) {
                "ok" -> activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.nav_host_fragment, MenuFragment())
                    ?.commit()

                else -> Toast.makeText(context, "Неверный логин/пароль", Toast.LENGTH_LONG)
                    .show()
            }


        })
        viewModel.profile.observeFutureEvents(viewLifecycleOwner, Observer { profile ->
            if(profile.databaseID!=""){
                val text = "${tvProfile.text}\n ${profile.name}"
                tvProfile.text = text
            }else{
                sharedViewModel.updateProgressBar(false)
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.nav_host_fragment, StartFragment())
                    ?.commit()
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
            selectedLogin?.let { login ->
                if (login.equals("")){
                    Toast.makeText(requireContext(),"Выберите пользователя",Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.login(login, editTextPassword.text.toString())
                }
            }
        }
        editTextPassword.setOnEditorActionListener { textView, id, keyEvent ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                viewModel.login(tvSignName.text.toString(), editTextPassword.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

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

        buttonLoginQR.setOnClickListener {
            showScannerView()
        }

    }

    private fun showScannerView() {
        isScannerMode = true
        llToolbar.visibility = View.GONE
        scrollView.visibility = View.GONE
        flScanner.visibility = View.VISIBLE


        codeScanner.decodeCallback = DecodeCallback { result ->
            CoroutineScope(Dispatchers.IO).launch {


                withContext(Dispatchers.Main) {
                    Snackbar.make(requireView(), result.text.toString(), Snackbar.LENGTH_SHORT)
                        .show()
                    flScanner.visibility = View.GONE
                    llToolbar.visibility = View.VISIBLE
                    scrollView.visibility = View.VISIBLE
                    isScannerMode = false
                    codeScanner.releaseResources()
                }
            }
        }
        codeScanner.startPreview()
    }

    override fun onResume() {
        super.onResume()
        if (isScannerMode) codeScanner.startPreview()
    }

    override fun onPause() {
        if (isScannerMode) codeScanner.releaseResources()
        super.onPause()
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