package sitec_it.ru.androidapp.ui

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.ui.settings.SettingsContainerFragment
import sitec_it.ru.androidapp.viewModels.StartViewModel
import kotlin.system.exitProcess

@AndroidEntryPoint
class StartFragment: Fragment() {
    companion object{
        const val CHECK_PERMISSION_CODE: Int = 1
    }

    private val viewModel: StartViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.initDefaultProfile()
        val btnManual: Button = view.findViewById(R.id.btn_start_set_manually)
        val btnScan: Button = view.findViewById(R.id.btn_start_scan_qr)
        btnManual.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.nav_host_fragment, SettingsContainerFragment())
                ?.commit()
        }
        btnScan.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.nav_host_fragment, BarcodeScannerFragment())
                ?.addToBackStack(null)
                ?.commit()
        }
        if(!permissionsGranted()){
            permissionsRequest()
        }

    }

    fun permissionsGranted(): Boolean {

        return (checkSelfPermission(
            requireContext(), Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(
            requireContext(), Manifest.permission.CHANGE_WIFI_STATE
        ) == PackageManager.PERMISSION_GRANTED)
    }

    fun permissionsRequest() {
        requestPermissions(arrayOf<String>(Manifest.permission.CAMERA,), CHECK_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CHECK_PERMISSION_CODE) {
            var permissionDenied = false
            for (grantResult in grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    permissionDenied = true
                    break
                }
            }
            if (permissionDenied) {
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setCancelable(false)
                alertDialog.setTitle("Внимание")
                alertDialog.setMessage("Для корректной работы приложения необходимо принять запрашиваемые разрешения.")
                alertDialog.setPositiveButton("Повторить запрос") { dialog: DialogInterface, which: Int ->
                    dialog.cancel()
                    permissionsRequest()
                }
                alertDialog.setNegativeButton("Выйти") { dialog: DialogInterface?, which: Int ->
                    exitProcess(
                        0
                    )
                }
                alertDialog.show()
            } else {

            }
        }
    }


}