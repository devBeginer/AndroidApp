package sitec_it.ru.androidapp.ui

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.data.models.newForms1.Element
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

    private val cardViewIcons = arrayListOf<Int>(R.drawable.baseline_note_add_24, R.drawable.baseline_upload_file_24, R.drawable.baseline_local_grocery_store_24, R.drawable.baseline_assignment_turned_in_24)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            sharedViewModel.updateProgressBar(true)
            if (isMainMenu) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.nav_host_fragment, LoginFragment())?.commit()
            } else {
                viewModel.getMainForm()
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
        //glMenu = view.findViewById(R.id.gridMenu)
        scrollView = view.findViewById(R.id.mainScroll)
        mainContainer = view.findViewById(R.id.ll_main_container)
        //drawGridLayout()
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar_menu)
        //val card1 = view.findViewById<MaterialCardView>(R.id.card1)
        //val cardSettings = view.findViewById<MaterialCardView>(R.id.cardSettings)

        //toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        toolbar.setNavigationIcon(R.drawable.baseline_logout_24)
        toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.nav_host_fragment, LoginFragment())
                ?.commit()
        }

        initData()

        viewModel.nameForLabel.observe(viewLifecycleOwner, Observer {
            it?.let { userName ->
                Log.d("user", "current -> $userName")
                val textLabel = "Привет, $userName!"
                tvHello.text = textLabel
            }
        })

        viewModel.form.observe(viewLifecycleOwner, Observer { menuElements ->
            mainContainer.removeAllViews()
            if(menuElements.isNotEmpty() && menuElements[0].first == "Главное меню"){
                drawGridLayout()
            }
            menuElements.forEachIndexed { index, element ->
                if (element.first == "Главное меню") {
                    when (element.second) {
                        "Приемка" -> drawCardView(index + 100, index + 110, index + 120, element.second) {

                            sharedViewModel.updateProgressBar(true)
                            mainContainer.removeAllViews()
                            //drawSubmenu(arrayListOf("Создать предварительную приемку", "Предварительная приемка", "Закрыть предварительную приемку", "Создать приемку"))
                            viewModel.getSubMenu(element.second)
                        }

                        "Размещение" -> drawCardView(
                            index + 100,
                            index + 110,
                            index + 120,
                            element.second
                        ) {
                            sharedViewModel.updateProgressBar(true)
                            mainContainer.removeAllViews()
                            //drawSubmenu(arrayListOf("Универсальное размещение", "Размещение контейнеров", "Размещение товара"))
                            viewModel.getSubMenu(element.second)
                        }

                        "Перемещение" -> drawCardView(
                            index + 100,
                            index + 110,
                            index + 120,
                            element.second
                        ) {
                            sharedViewModel.updateProgressBar(true)
                            mainContainer.removeAllViews()
                            //(arrayListOf("Универсальное перемещение", "Перемещение контейнеров", "Свободное перемещение контейнеров", "Перемещение товара"))
                            viewModel.getSubMenu(element.second)
                        }

                        "Отбор" -> drawCardView(index + 100, index + 110, index + 120, element.second) {
                            sharedViewModel.updateProgressBar(true)
                            mainContainer.removeAllViews()
                            //drawSubmenu(arrayListOf("Отбор товара", "Отбор контейнеров", "Кластерный отбор", "Консолидация контейнеров"))
                            viewModel.getSubMenu(element.second)
                        }

                        "Отгрузка" -> drawCardView(index + 100, index + 110, index + 120, element.second) {
                            sharedViewModel.updateProgressBar(true)
                            mainContainer.removeAllViews()
                            //drawSubmenu(arrayListOf("Сортировка", "Упаковка", "Контроль отгрузки", "Отгрузка"))
                            viewModel.getSubMenu(element.second)
                        }

                        "Подпитка" -> drawCardView(index + 100, index + 110, index + 120, element.second) {
                            sharedViewModel.updateProgressBar(true)
                            mainContainer.removeAllViews()
                            //drawSubmenu(arrayListOf("Подпитка контейнеров", "Подпитка товара"))
                            viewModel.getSubMenu(element.second)
                        }

                        "Регламентные" -> drawCardView(
                            index + 100,
                            index + 110,
                            index + 120,
                            element.second
                        ) {
                            sharedViewModel.updateProgressBar(true)
                            mainContainer.removeAllViews()
                            //drawSubmenu(arrayListOf("Контроль качества", "Инвентаризация", "Свободная инвентаризация контейнера", "Маркировка"))
                            viewModel.getSubMenu(element.second)
                        }

                        "Сервисные" -> drawCardView(
                            index + 100,
                            index + 110,
                            index + 120,
                            element.second
                        ) {
                            sharedViewModel.updateProgressBar(true)
                            mainContainer.removeAllViews()
                            //drawSubmenu(arrayListOf("Ввод остатков", "Остатки товаров", "Ввод вгх", "Ввод шк"))
                            viewModel.getSubMenu(element.second)
                        }
                    }
                } else {
                    drawSubmenu(element.second)
                }

            }
            sharedViewModel.updateProgressBar(false)
        })

        viewModel.menuForms.observe(viewLifecycleOwner, Observer {form->
            if(form!=null){
                mainContainer.removeAllViews()
                if(form.FormName == "Главное меню"){
                    drawGridLayout()
                }
                val menuElements = form.Elements
                drawForm(menuElements.reversed(), form.FormName)
                sharedViewModel.updateProgressBar(false)
            }

        })
        /*card1.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.image_click)
            )
        }
        cardSettings.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.image_click)
            )
        }*/
    }

    private fun initData() {
        sharedViewModel.updateProgressBar(true)
        viewModel.setTvHello()
        //viewModel.getMainForm()
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
    }

    private fun drawForm(menuElements: List<Element>, formType: String){
        menuElements.forEachIndexed { index, element ->
            when(element.ElementType){
                "Button"->{
                    if(formType=="Главное меню"){
                        drawCardView(index + 100, index + 110, index + 120, element.ElementName) {
                            sharedViewModel.updateProgressBar(true)
                            //mainContainer.removeAllViews()
                            val nextForm = element.Actions
                                .find { action -> action.Action == "GoToForm" }
                                ?.Arguments?.find { argument -> argument.Name == "FormID"  }
                            nextForm?.let { argument ->  viewModel.getSubMenuForm(argument.Value) } ?: sharedViewModel.updateProgressBar(false)
                        }
                    }else{
                        drawSubmenu(element.ElementName)
                    }
                }
                "Field"->{}
                "Table"->{}
                "SelectionField"->{}
                "Text"->{}
            }
        }
    }

    private fun drawGridLayout() {
        isMainMenu = true
        val gridLayout = GridLayout(context)
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
        cardId: Int,
        imageId: Int,
        textId: Int,
        text: String,
        onClick: () -> Unit
    ) {
        val cardView = MaterialCardView(context)
        //var gridLayoutParams = GridLayout.LayoutParams(glMenu.layoutParams)
        val gridLayoutParams = GridLayout.LayoutParams(
            GridLayout.spec(GridLayout.UNDEFINED, 1f),
            GridLayout.spec(GridLayout.UNDEFINED, 1f)
        )
        gridLayoutParams.width = GridLayout.LayoutParams.WRAP_CONTENT
        gridLayoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT
        val margin = (5 * Resources.getSystem().displayMetrics.density).toInt()
        gridLayoutParams.setMargins(margin, margin, margin, margin)

        cardView.layoutParams = gridLayoutParams
        cardView.setCardBackgroundColor(Color.WHITE)
        cardView.radius = 12 * Resources.getSystem().displayMetrics.density
        cardView.elevation = 6 * Resources.getSystem().displayMetrics.density
        cardView.id = cardId

        val linearLayout = LinearLayout(context)
        linearLayout.minimumHeight = (160 * Resources.getSystem().displayMetrics.density).toInt()
        val linearLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        linearLayoutParams.gravity = Gravity.CENTER_HORIZONTAL
        linearLayout.layoutParams = linearLayoutParams
        linearLayout.orientation = LinearLayout.VERTICAL

        val imageView = ImageView(context)
        imageView.id = imageId
        imageView.setImageDrawable(context?.let {
            ContextCompat.getDrawable(
                it,
                /*R.drawable.baseline_note_add_24*/
                cardViewIcons.random()
            )
        })
        imageView.setColorFilter(
            Color.parseColor("#03A9F4"),
            android.graphics.PorterDuff.Mode.MULTIPLY
        )
        imageView.minimumHeight = (135 * Resources.getSystem().displayMetrics.density).toInt()
        //imageView.adjustViewBounds = true
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        imageView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            3f
        )

        val textView = TextView(context)
        textView.id = textId
        textView.text = text
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        //textView.layoutParams = linearLayoutParams
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
        textView.maxWidth = (75 * Resources.getSystem().displayMetrics.density).toInt()
        //textView.maxEms = 3

        linearLayout.addView(imageView)
        linearLayout.addView(textView)
        cardView.addView(linearLayout)
        glMenu.addView(cardView)
        cardView.setOnClickListener {
            onClick()
        }

        /*val gridLayoutParams = glMenu.layoutParams as GridLayout.LayoutParams
        gridLayoutParams.rowSpec =
        cardView.layoutParams = GridLayout.LayoutParams(
            GridLayout.LayoutParams.WRAP_CONTENT,
            GridLayout.LayoutParams.WRAP_CONTENT
        )*/

    }

    private fun drawSubmenu(subAction: String) {
        isMainMenu = false

        val button = Button(context)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val margin = (5 * Resources.getSystem().displayMetrics.density).toInt()
        layoutParams.setMargins(margin, margin, margin, margin)
        button.layoutParams = layoutParams
        button.text = subAction
        mainContainer.addView(button)

    }
}