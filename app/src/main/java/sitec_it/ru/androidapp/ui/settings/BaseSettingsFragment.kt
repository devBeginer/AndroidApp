package sitec_it.ru.androidapp.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Switch
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.data.models.Profile
import sitec_it.ru.androidapp.viewModels.BaseSettingsViewModel
@AndroidEntryPoint
class BaseSettingsFragment:Fragment(R.layout.fragment_base_settings) {
    private val viewModel: BaseSettingsViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initView("По умолчанию")

        val server: EditText = view.findViewById(R.id.et_base_settings_server)
        val port: EditText = view.findViewById(R.id.et_base_settings_port)
        val base: EditText = view.findViewById(R.id.et_base_settings_base)
        val login: EditText = view.findViewById(R.id.et_base_settings_login)
        val password: EditText = view.findViewById(R.id.et_base_settings_password)
        val ssl: Switch = view.findViewById(R.id.switch_base_settings_UseHTTPS)
        var currentProfile: Profile? = null

        viewModel.user.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                server.setText(profile.server)
                port.setText(profile.port)
                base.setText(profile.base)
                login.setText(profile.login)
                password.setText(profile.password)
                ssl.isChecked = profile.ssl
                currentProfile = profile
            }
        }

        server.doAfterTextChanged {editable->
            currentProfile?.let { profile ->
                profile.server = editable.toString()
                viewModel.updateProfile(profile) }
        }
        port.doAfterTextChanged {editable->
            currentProfile?.let { profile ->
                profile.port = editable.toString()
                viewModel.updateProfile(profile) }
        }
        base.doAfterTextChanged {editable->
            currentProfile?.let { profile ->
                profile.base = editable.toString()
                viewModel.updateProfile(profile) }
        }
        login.doAfterTextChanged {editable->
            currentProfile?.let { profile ->
                profile.login = editable.toString()
                viewModel.updateProfile(profile) }
        }
        password.doAfterTextChanged {editable->
            currentProfile?.let { profile ->
                profile.password = editable.toString()
                viewModel.updateProfile(profile) }
        }

        ssl.setOnCheckedChangeListener { compoundButton, isChecked ->
            currentProfile?.let { profile ->
                currentProfile?.ssl = isChecked
                viewModel.updateProfile(profile) }
        }


    }


}