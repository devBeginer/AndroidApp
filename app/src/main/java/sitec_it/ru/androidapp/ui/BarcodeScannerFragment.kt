package sitec_it.ru.androidapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.viewModels.SharedViewModel

class BarcodeScannerFragment : Fragment() {
    private lateinit var codeScanner: CodeScanner
    private val sharedViewModel: SharedViewModel by viewModels({requireActivity()})
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_barcode_scanner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback { result ->
            /*activity.runOnUiThread {
                Toast.makeText(activity, result.text, Toast.LENGTH_LONG).show()
            }*/
            sharedViewModel.postScanResult(result.text)
            activity.supportFragmentManager.popBackStack()
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}