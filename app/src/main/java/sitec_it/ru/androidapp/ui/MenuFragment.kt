package sitec_it.ru.androidapp.ui

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.TypeForms
import sitec_it.ru.androidapp.data.models.menu.BodyForRequest
import sitec_it.ru.androidapp.data.models.menu.GeneralForm
import sitec_it.ru.androidapp.data.models.menu.GeneralFormAction
import sitec_it.ru.androidapp.data.models.menu.GeneralFormElement
import sitec_it.ru.androidapp.data.models.menu.MenuForm
import sitec_it.ru.androidapp.data.models.menu.MenuFormElement
import sitec_it.ru.androidapp.data.models.menu.RemoteFormRequest
import sitec_it.ru.androidapp.data.models.menu.SubMenu
import sitec_it.ru.androidapp.data.models.menu.SubMenuAction
import sitec_it.ru.androidapp.viewModels.MenuViewModel
import sitec_it.ru.androidapp.viewModels.SharedViewModel


@AndroidEntryPoint
class MenuFragment : Fragment() {

    private val viewModel: MenuViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels({ requireActivity() })
    private lateinit var tvTest: TextView
    private lateinit var glMenu: GridLayout
    private lateinit var scrollView: ScrollView
    private lateinit var mainContainer: LinearLayout
    private var isMainMenu = true
    private var currentTypeForm: TypeForms = TypeForms.MENU
    var menuForm: MenuForm? = null
    var generalForm: List<GeneralForm>? = null
    var currentThreadID: String? = ""
    lateinit var toolbar:MaterialToolbar
    val mapArguments = HashMap<String,String>()

    private val cardViewIcons = arrayListOf<Int>(
        R.drawable.baseline_note_add_24,
        R.drawable.baseline_upload_file_24,
        R.drawable.baseline_local_grocery_store_24,
        R.drawable.baseline_assignment_turned_in_24
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            // sharedViewModel.updateProgressBar(true)
            when (currentTypeForm) {
                TypeForms.SUBMENU -> {
                    menuForm?.Elements?.let { drawMainMenu(it) }
                }

                TypeForms.MENU -> {
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.nav_host_fragment, LoginFragment())?.commit()
                }

                TypeForms.GENERALFORM -> {
                    menuForm?.Elements?.let { drawMainMenu(it) }
                }
            }


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvHello = view.findViewById<TextView>(R.id.tv_hello)
        tvTest = view.findViewById(R.id.test_tv)
        scrollView = view.findViewById(R.id.mainScroll)
        mainContainer = view.findViewById(R.id.ll_main_container)
        toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar_menu)


        toolbar.setNavigationIcon(R.drawable.baseline_menu_24)
        toolbar.setNavigationOnClickListener {
            val actionMenuDialogFragment = ActionMenuDialogFragment()
            requireActivity().supportFragmentManager
                .let { manager ->
                    actionMenuDialogFragment.show(manager, actionMenuDialogFragment.tag)
                }
        }

        initData()

        viewModel.nameForLabel.observe(viewLifecycleOwner, Observer {
            it?.let { userName ->
                Log.d("user", "current -> $userName")
                val textLabel = "Привет, $userName!"
                tvHello.text = textLabel
            }
        })

        /* viewModel.menuForms.observe(viewLifecycleOwner, Observer { form ->
             if (form != null) {
                 mainContainer.removeAllViews()

                 val menuElements = form.Elements
                 if (form.FormName == "Главное меню") {
                     drawGridLayout()
                 }
              //   drawForm(menuElements, form.FormName)
                 sharedViewModel.updateProgressBar(false)
             }

         })*/

    }

    private fun initData() {
        sharedViewModel.updateProgressBar(true)
        viewModel.setTvHello()
        viewModel.loadForms()
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.updateProgressBar(true)
        viewModel.getChanges()
        viewModel.changes.observe(viewLifecycleOwner, Observer {
            tvTest.text = it.toString()
            sharedViewModel.updateProgressBar(false)
        })
        viewModel.error.observe(viewLifecycleOwner, Observer { value ->
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
            }
        })

        viewModel.form.observe(viewLifecycleOwner, Observer { forms ->
            forms?.let { allData ->
                generalForm = allData.GeneralForms
                menuForm = allData.MenuForms.find { form -> form.FormType == "menu" }
                // currentThreadID = formMenu?.FormID
                val formName = menuForm?.FormName

                menuForm?.let { useFormMenu ->
                    Log.d("drawForm", "нашли главное меню")
                    toolbar.title = menuForm?.FormName
                    drawMainMenu(useFormMenu.Elements)
                }
            }
        })

        /*sharedViewModel.menuForms.observe(viewLifecycleOwner, Observer {
            it?.let { allData ->
                generalForm = allData.GeneralForms
                menuForm = allData.MenuForms.find { form -> form.FormType == "menu" }
                // currentThreadID = formMenu?.FormID
                val formName = menuForm?.FormName

                menuForm?.let { useFormMenu ->
                    Log.d("drawForm", "нашли главное меню")
                    toolbar.title = menuForm?.FormName
                    drawMainMenu(useFormMenu.Elements)
                }
            }
        })*/
    }

    private fun drawMainMenu(elements: List<MenuFormElement>) {
        mainContainer.removeAllViews()
        drawGridLayout()
        currentTypeForm = TypeForms.MENU
        toolbar.title = menuForm?.FormName
        elements.forEach { elementForm ->

            when (elementForm.ElementType) {
                "Button" -> {
                    drawCardView(elementForm.Text) { view ->
                        view?.startAnimation(
                            AnimationUtils.loadAnimation(requireContext(), R.anim.image_click)
                        )

                        // отрисовка подменю по нажатию
                        mainContainer.removeAllViews()
                        drawSubMenu(elementForm.SubMenu, elementForm.Text)
                    }
                }
            }
        }

        sharedViewModel.updateProgressBar(false)
    }

    private fun drawSubMenu(
        subMenu: List<SubMenu>,
        formName: String?
    ) {
        currentTypeForm = TypeForms.SUBMENU
        mainContainer.removeAllViews()
        toolbar.title = formName
        subMenu.forEach { element ->
            when (element.ElementType) {
                "Button" -> {
                    drawButton(element.Text, element.Actions)
                }
            }

        }
        sharedViewModel.updateProgressBar(false)
    }

    private fun drawGridLayout() {
        isMainMenu = true
        val gridLayout = GridLayout(requireContext())
        val gridLayoutParams = GridLayout.LayoutParams()
        gridLayoutParams.width = GridLayout.LayoutParams.MATCH_PARENT
        gridLayoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT
        gridLayoutParams.marginStart = (4 * Resources.getSystem().displayMetrics.density).toInt()
        gridLayoutParams.marginEnd = (4 * Resources.getSystem().displayMetrics.density).toInt()
        gridLayout.columnCount = 2
        mainContainer.addView(gridLayout)
        glMenu = gridLayout
    }

    private fun drawCardView(
        text: String,
        onClick: (View?) -> Unit
    ) {
        val cardView = MaterialCardView(requireContext())
        val gridLayoutParams = GridLayout.LayoutParams(
            GridLayout.spec(GridLayout.UNDEFINED, 1f),
            GridLayout.spec(GridLayout.UNDEFINED, 1f)
        )
        gridLayoutParams.width = 0
        gridLayoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT
        val margin = (7 * Resources.getSystem().displayMetrics.density).toInt()
        gridLayoutParams.setMargins(margin, margin, margin, margin)

        cardView.layoutParams = gridLayoutParams
        cardView.setCardBackgroundColor(Color.WHITE)
        cardView.radius = 12 * Resources.getSystem().displayMetrics.density
        cardView.elevation = 6 * Resources.getSystem().displayMetrics.density
        cardView.id = View.generateViewId()

        val linearLayout = LinearLayout(requireContext())
        linearLayout.minimumHeight = (160 * Resources.getSystem().displayMetrics.density).toInt()
        val linearLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        linearLayoutParams.gravity = Gravity.CENTER_HORIZONTAL
        linearLayout.layoutParams = linearLayoutParams
        linearLayout.orientation = LinearLayout.VERTICAL

        val imageView = ImageView(requireContext())
        imageView.id = View.generateViewId()
        imageView.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                cardViewIcons.random()
            )
        )
        imageView.setColorFilter(
            Color.parseColor("#03A9F4"),
            android.graphics.PorterDuff.Mode.MULTIPLY
        )
        imageView.minimumHeight = (135 * Resources.getSystem().displayMetrics.density).toInt()
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        imageView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            3f
        )

        val textView = TextView(requireContext())
        textView.id = View.generateViewId()
        textView.text = text
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        val textViewParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        textViewParams.setMargins(
            0,
            0,
            0,
            (8 * Resources.getSystem().displayMetrics.density).toInt()
        )
        textViewParams.gravity = Gravity.CENTER_HORIZONTAL
        textView.layoutParams = textViewParams
        textView.gravity = Gravity.CENTER_HORIZONTAL

        linearLayout.addView(imageView)
        linearLayout.addView(textView)
        cardView.addView(linearLayout)
        glMenu.addView(cardView)
        cardView.setOnClickListener {
            onClick(cardView)
        }

    }

    private fun drawButton(text: String, actions: List<SubMenuAction>) {
        isMainMenu = false

        val button = Button(requireContext())
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val margin = (5 * Resources.getSystem().displayMetrics.density).toInt()
        layoutParams.setMargins(margin, margin, margin, margin)
        button.layoutParams = layoutParams
        button.text = text
        button.setBackgroundColor(Color.parseColor("#03A9F4"))
        button.setOnClickListener {
            Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT).show()
            actions.forEach { itemAction ->
                when(itemAction.Action){
                    "Save" -> {
                        mapArguments[itemAction.Argument] = itemAction.Value
                        currentThreadID = itemAction.Value
                    }
                    "GoToForm" -> {
                        Log.d("drawForm", "subMenu itemAction.Action -->${itemAction.Action}")
                        Log.d("drawForm", "subMenu itemAction.Value -->${itemAction.Value}")
                        // переход на другую форму
                        // достать форму из БД и перейти на нее
                        val form = generalForm?.find { form -> form.FormType == "generalForm" }
                        if (form != null) {
                            if (form.FormID.equals(itemAction.Value)){
                                drawForm(form.FormDescription,form.Elements,form.RemoteFormRequest)
                            }
                        }

                    }
                }
            }
        }
        mainContainer.addView(button)

    }

    private fun drawForm(
        formDescription: String,
        elements: List<GeneralFormElement>,
        remoteFormRequest: List<RemoteFormRequest>
    ) {
        currentTypeForm = TypeForms.GENERALFORM
        mainContainer.removeAllViews()
        toolbar.title = formDescription
        elements.forEach { elementForm ->
            when(elementForm.ElementType){
                "TextView" -> {
                    drawTextView(elementForm.Text,elementForm.Actions)
                }
                "EditText" -> {
                    drawEditText(elementForm.Text,elementForm.Actions,remoteFormRequest)
                }
            }
        }

    }

    private fun drawEditText(
        text: String,
        actions: List<GeneralFormAction>,
        remoteFormRequest: List<RemoteFormRequest>
    ) {
        val et = EditText(requireContext())
        val etLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        et.gravity = Gravity.CENTER_HORIZONTAL
        et.layoutParams = etLayoutParams
        et.setHint(text)
        et.setTextSize(19f)
        et.isSingleLine = true
        et.setImeActionLabel("Custom text", KeyEvent.KEYCODE_ENTER);
      /*  et.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN) {
               // сделать запрос и перейти на новый фрагмент и форму
                actions.forEach { action ->
                    when(action.Action){
                        "Save" -> {
                            if (action.Argument != null)
                                mapArguments[action.Argument] = et.text.toString()
                        }
                        "gеtRemoteForm" -> {
                        val request = remoteFormRequest.find { request -> request.FormID.equals(action.Value) }
                           val listArguments = mutableListOf<String>()
                            request?.Arguments?.forEach { item ->
                                when(item){
                                    "ThreadID" -> {
                                        mapArguments["ThreadID"]?.let { listArguments.add(it) }
                                    }
                                    "GateID" -> {
                                        mapArguments["GateID"]?.let { listArguments.add(it) }
                                    }
                                }
                            }
                            val body = BodyForRequest(
                                request?.FormID.toString(),
                                listArguments
                            )
                        Toast.makeText(requireContext(),body.toString(),Toast.LENGTH_SHORT).show()

                        }
                    }

                }
                return@setOnKeyListener true
            }
            false
        }*/
        et.setOnEditorActionListener { textView, i, keyEvent ->
            actions.forEach { action ->
                when(action.Action){
                    "Save" -> {
                        if (action.Argument != null)
                            mapArguments[action.Argument] = et.text.toString()
                    }
                    "gеtRemoteForm" -> {
                        val request = remoteFormRequest.find { request -> request.FormID.equals(action.Value) }
                        val listArguments = mutableListOf<String>()
                        request?.Arguments?.forEach { item ->
                            when(item){
                                "ThreadID" -> {
                                    mapArguments["ThreadID"]?.let { listArguments.add(it) }
                                }
                                "GateID" -> {
                                    mapArguments["GateID"]?.let { listArguments.add(it) }
                                }
                            }
                        }
                        val body = BodyForRequest(
                            request?.FormID.toString(),
                            listArguments
                        )
                        Toast.makeText(requireContext(),body.toString(),Toast.LENGTH_SHORT).show()

                    }
                }

            }
            false
        }
        mainContainer.addView(et)
    }

    private fun drawTextView(text: String, actions: List<GeneralFormAction>) {
        val tv = MaterialTextView(requireContext())
        val tvLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        tv.gravity = Gravity.CENTER_HORIZONTAL
        tv.layoutParams = tvLayoutParams
        tv.setText(text)
        tv.setTextSize(19f)

        mainContainer.addView(tv)
    }
}