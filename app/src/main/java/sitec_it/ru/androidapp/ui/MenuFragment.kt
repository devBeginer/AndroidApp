package sitec_it.ru.androidapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.Utils.observeFutureEvents
import sitec_it.ru.androidapp.viewModels.MenuViewModel
import sitec_it.ru.androidapp.viewModels.SharedViewModel

@AndroidEntryPoint
class MenuFragment : Fragment() {

    private val viewModel: MenuViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels({requireActivity()})
    lateinit var tvTest: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.nav_host_fragment, LoginFragment())?.commit()
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
        tvTest = view.findViewById<TextView>(R.id.test_tv)
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar_menu)
        val card1 = view.findViewById<MaterialCardView>(R.id.card1)
        val cardSettings = view.findViewById<MaterialCardView>(R.id.cardSettings)

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
        card1.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.image_click)
            )
        }
        cardSettings.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.image_click)
            )
        }
    }

    private fun initData() {
        viewModel.setTvHello()
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.updateProgressBar(true)
        viewModel.getChanges()
        viewModel.changes.observe(viewLifecycleOwner, Observer {
            tvTest.setText(it.toString())
            sharedViewModel.updateProgressBar(false)
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {value->
            if (value != null && value == "ERROR - Connection") {
                val snackbar: Snackbar = Snackbar.make(
                    requireView(),
                    "Нет подключения к интернету.", Snackbar.LENGTH_LONG
                )
                val view = snackbar.view
                val txtv =
                    view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
                txtv.maxLines = 5
                snackbar.show()
            }
        })
    }
}