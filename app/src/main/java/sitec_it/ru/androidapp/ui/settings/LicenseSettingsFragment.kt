package sitec_it.ru.androidapp.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.data.models.ProfileLicense
import sitec_it.ru.androidapp.viewModels.LicenseSettingsViewModel

@AndroidEntryPoint
class LicenseSettingsFragment: Fragment(R.layout.fragment_license_settings) {

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

        server.doAfterTextChanged {editable->
            currentProfileLicense?.let { profile ->
                profile.server = editable.toString()
                viewModel.updateProfile(profile) }
        }
        port.doAfterTextChanged {editable->
            currentProfileLicense?.let { profile ->
                profile.port = editable.toString()
                viewModel.updateProfile(profile) }
        }
        login.doAfterTextChanged {editable->
            currentProfileLicense?.let { profile ->
                profile.login = editable.toString()
                viewModel.updateProfile(profile) }
        }
        password.doAfterTextChanged {editable->
            currentProfileLicense?.let { profile ->
                profile.password = editable.toString()
                viewModel.updateProfile(profile) }
        }


    }
}