package sitec_it.ru.androidapp.ui.settings

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.activity.addCallback
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.SettingsParser
import sitec_it.ru.androidapp.data.models.DialogParams
import sitec_it.ru.androidapp.ui.LoginFragment
import sitec_it.ru.androidapp.viewModels.SharedViewModel

@AndroidEntryPoint
class SettingsContainerFragment: Fragment(R.layout.fragment_settings_container) {
    private val sharedViewModel: SharedViewModel by viewModels({requireActivity()})
    private var isScannerMode: Boolean = false
    private lateinit var codeScanner: CodeScanner
    private lateinit var flScanner: FrameLayout
    private lateinit var group: Group

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This callback is only called when MyFragment is at least started
        /*val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
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
        }*/
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        group = view.findViewById(R.id.viewGroup_settings)
        flScanner = view.findViewById(R.id.frameLayout_settings_scan_qr)
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        codeScanner = CodeScanner(requireActivity(), scannerView)

        val bottomNavigationView: BottomNavigationView = view.findViewById(R.id.settings_bottom_navigation_view)
        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            var selectedFragment = when (item.itemId) {
                R.id.baseFragment -> BaseSettingsFragment()
                R.id.licenseFragment -> LicenseSettingsFragment()
                R.id.additionFragment -> AdditionSettingsFragment()
                else -> null
            }
            if (selectedFragment != null) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.settings_nav_host_fragment, selectedFragment)?.commit()
            }

            true
        }


        if (savedInstanceState == null) activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.settings_nav_host_fragment, BaseSettingsFragment())?.commit()


        sharedViewModel.scanMode.observe(viewLifecycleOwner, Observer {enable->
            if(enable){
                if(!isScannerMode) showScannerView()
            }else{
                if (isScannerMode) {
                    flScanner.visibility = View.GONE
                    group.visibility = View.VISIBLE
                    isScannerMode = false
                    codeScanner.releaseResources()
                }
            }
        })
    }

    private fun showScannerView() {
        isScannerMode = true
        group.visibility = View.GONE
        flScanner.visibility = View.VISIBLE


        codeScanner.decodeCallback = DecodeCallback { result ->
            CoroutineScope(Dispatchers.IO).launch {


                withContext(Dispatchers.Main) {
                    Snackbar.make(requireView(), result.text.toString(), Snackbar.LENGTH_SHORT)
                        .show()
                    val settingsParser = SettingsParser()

                    try {
                        val settings = settingsParser.parseSettings(result.text.toString())
                        sharedViewModel.updateSettings(settings)
                    } catch (e: JsonSyntaxException) {
                        Snackbar.make(requireView(), "Некорректный QR-код", Snackbar.LENGTH_SHORT)
                            .show()
                    } catch (e: IllegalStateException) {
                        Snackbar.make(requireView(), "Некорректный QR-код", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                    flScanner.visibility = View.GONE
                    group.visibility = View.VISIBLE
                    isScannerMode = false
                    codeScanner.releaseResources()
                }
            }
        }
        codeScanner.startPreview()
    }
}