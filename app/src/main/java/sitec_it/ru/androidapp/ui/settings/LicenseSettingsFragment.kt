package sitec_it.ru.androidapp.ui.settings

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.elconfidencial.bubbleshowcase.BubbleShowCase
import com.elconfidencial.bubbleshowcase.BubbleShowCase.ArrowPosition
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder
import com.elconfidencial.bubbleshowcase.BubbleShowCaseListener
import dagger.hilt.android.AndroidEntryPoint
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.data.models.ProfileLicense
import sitec_it.ru.androidapp.ui.LoginFragment
import sitec_it.ru.androidapp.viewModels.LicenseSettingsViewModel
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt.PromptStateChangeListener
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal


@AndroidEntryPoint
class LicenseSettingsFragment : Fragment(R.layout.fragment_license_settings) {

    private val viewModel: LicenseSettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initView()

        val server: EditText = view.findViewById(R.id.et_license_settings_server)
        val port: EditText = view.findViewById(R.id.et_license_settings_port)
        val login: EditText = view.findViewById(R.id.et_license_settings_login)
        val password: EditText = view.findViewById(R.id.et_license_settings_password)
        var currentProfileLicense: ProfileLicense? = null

        viewModel.user.observe(viewLifecycleOwner) { profileLicense ->
            if (profileLicense != null) {
                server.setText(profileLicense.server)
                port.setText(profileLicense.port)
                login.setText(profileLicense.login)
                password.setText(profileLicense.password)
                currentProfileLicense = profileLicense
            }
        }

        server.doAfterTextChanged { editable ->
            currentProfileLicense?.let { profile ->
                profile.server = editable.toString()
                viewModel.updateProfile(profile)
            }
        }
        port.doAfterTextChanged { editable ->
            currentProfileLicense?.let { profile ->
                profile.port = editable.toString()
                viewModel.updateProfile(profile)
            }
        }
        login.doAfterTextChanged { editable ->
            currentProfileLicense?.let { profile ->
                profile.login = editable.toString()
                viewModel.updateProfile(profile)
            }
        }
        password.doAfterTextChanged { editable ->
            currentProfileLicense?.let { profile ->
                profile.password = editable.toString()
                viewModel.updateProfile(profile)
            }
        }


        activity?.let {
            MaterialTapTargetPrompt.Builder(it)
                .setTarget(server)
                .setPrimaryText("Сервер")
                .setSecondaryText("Введите адрес сервера")
                .setPromptStateChangeListener(PromptStateChangeListener { prompt, state ->
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                        // User has pressed the prompt target
                    }
                })
                //.setPromptBackground(RectanglePromptBackground())
                .setPromptFocal(RectanglePromptFocal())
                .show()
        }


        port.setOnClickListener {
            activity?.let {activity->
                MaterialTapTargetPrompt.Builder(activity)
                    .setTarget(port)
                    .setPrimaryText("Порт")
                    .setSecondaryText("Введите порт сервера лицензирования")
                    .setPromptStateChangeListener(PromptStateChangeListener { prompt, state ->
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                            // User has pressed the prompt target
                        }
                    })
                    //.setPromptBackground(RectanglePromptBackground())
                    .setPromptFocal(RectanglePromptFocal())
                    .show()
            }
        }



        login.setOnFocusChangeListener { view, focused ->
            if(focused){
                activity?.let {activity->
                    MaterialTapTargetPrompt.Builder(activity)
                        .setTarget(login)
                        .setPrimaryText("Логин")
                        .setSecondaryText("Введите логин сервера лицензирования")
                        .setPromptStateChangeListener(PromptStateChangeListener { prompt, state ->
                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                                // User has pressed the prompt target
                            }
                        })
                        //.setPromptBackground(RectanglePromptBackground())
                        .setPromptFocal(RectanglePromptFocal())
                        .show()
                }
            }

        }



        val back = view.findViewById<ImageView>(R.id.iv_license_settings_back)
        back.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.nav_host_fragment, LoginFragment())?.commit()
        }


    }
}