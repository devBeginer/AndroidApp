package sitec_it.ru.androidapp.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.ui.settings.SettingsContainerFragment

class ActionMenuDialogFragment: DialogFragment() {

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        return inflater.inflate(R.layout.dialog_fragment_action_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val close = view.findViewById<ImageView>(R.id.iv_action_dialog_close)
        val btnExit = view.findViewById<Button>(R.id.btn_action_dialog_exit)
        val btnSettings = view.findViewById<Button>(R.id.btn_action_dialog_settings)

        close.setOnClickListener {
            dismiss()
        }

        btnSettings.setOnClickListener {
            dismiss()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.nav_host_fragment, SettingsContainerFragment())
                ?.commit()
        }

        btnExit.setOnClickListener {
            dismiss()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.nav_host_fragment, LoginFragment())
                ?.commit()
        }
    }
}