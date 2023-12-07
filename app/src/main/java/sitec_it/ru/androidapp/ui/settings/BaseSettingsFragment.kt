package sitec_it.ru.androidapp.ui.settings

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import sitec_it.ru.androidapp.R
import sitec_it.ru.androidapp.data.models.Profile
import sitec_it.ru.androidapp.data.models.ProfileSpinnerItem
import sitec_it.ru.androidapp.ui.MainFragment
import sitec_it.ru.androidapp.viewModels.BaseSettingsViewModel
import java.util.ArrayList

@AndroidEntryPoint
class BaseSettingsFragment : Fragment(R.layout.fragment_base_settings) {
    private val viewModel: BaseSettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initView()

        val server: EditText = view.findViewById(R.id.et_base_settings_server)
        val port: EditText = view.findViewById(R.id.et_base_settings_port)
        val base: EditText = view.findViewById(R.id.et_base_settings_base)
        val login: EditText = view.findViewById(R.id.et_base_settings_login)
        val password: EditText = view.findViewById(R.id.et_base_settings_password)
        val ssl: Switch = view.findViewById(R.id.switch_base_settings_UseHTTPS)
        val notCheckCertificate: Switch = view.findViewById(R.id.switch_base_settings_DisableCheckSSL)
        val spinnerProfile: Spinner = view.findViewById(R.id.spinner_base_name)
        val btnNewProfile: Button = view.findViewById(R.id.btn_base_settings_new_profile)
        val btnDeleteProfile: Button = view.findViewById(R.id.btn_base_settings_delete_profile)
        var currentProfile: Profile? = null

        viewModel.user.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                currentProfile = profile
                server.setText(profile.server)
                port.setText(profile.port)
                base.setText(profile.base)
                login.setText(profile.login)
                password.setText(profile.password)
                ssl.isChecked = profile.ssl
                notCheckCertificate.isChecked = profile.notCheckCertificate

            }
        }

        var profileList = arrayListOf<ProfileSpinnerItem>()
        var arrayAdapter = ArrayAdapter<ProfileSpinnerItem>(
            requireContext(),
            //android.R.layout.simple_spinner_item,
            R.layout.custom_spinner_item,
            profileList
        )

        arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_drop_down_item)
        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerProfile.adapter = arrayAdapter
        viewModel.profileList.observe(viewLifecycleOwner) { list ->
            if (list != null) {
                arrayAdapter.clear()
                profileList = ArrayList(list)
                arrayAdapter.addAll(profileList)
                arrayAdapter.notifyDataSetChanged()
                val current = list.find { it.id == (currentProfile?.id ?: 1) }
                spinnerProfile.setSelection(list.indexOf(current))
            }
        }

        server.doAfterTextChanged { editable ->
            currentProfile?.let { profile ->
                profile.server = editable.toString()
                viewModel.updateProfile(profile)
            }
        }
        port.doAfterTextChanged { editable ->
            currentProfile?.let { profile ->
                profile.port = editable.toString()
                viewModel.updateProfile(profile)
            }
        }
        base.doAfterTextChanged { editable ->
            currentProfile?.let { profile ->
                profile.base = editable.toString()
                viewModel.updateProfile(profile)
            }
        }
        login.doAfterTextChanged { editable ->
            currentProfile?.let { profile ->
                profile.login = editable.toString()
                viewModel.updateProfile(profile)
            }
        }
        password.doAfterTextChanged { editable ->
            currentProfile?.let { profile ->
                profile.password = editable.toString()
                viewModel.updateProfile(profile)
            }
        }

        ssl.setOnCheckedChangeListener { compoundButton, isChecked ->
            currentProfile?.let { profile ->
                currentProfile?.ssl = isChecked
                viewModel.updateProfile(profile)
            }
        }

        notCheckCertificate.setOnCheckedChangeListener { compoundButton, isChecked ->
            currentProfile?.let { profile ->
                currentProfile?.notCheckCertificate = isChecked
                viewModel.updateProfile(profile)
            }
        }

        spinnerProfile.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent != null) {
                    viewModel.initView((parent.selectedItem as ProfileSpinnerItem).id)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        btnNewProfile.setOnClickListener {

            val promptsView: View =
                LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_new_profile, null)
            val recoveryAlert = MaterialAlertDialogBuilder(requireContext())
            recoveryAlert.setView(promptsView)
            val nameField =
                promptsView.findViewById<View>(R.id.et_dialog_profile_name) as EditText

            recoveryAlert
                .setTitle("Создать")
                .setMessage("Введите название нового профиля")
                .setPositiveButton("Создать", null)
                .setNegativeButton("Отмена") { dialog, which -> }

            val dialog = recoveryAlert.create()

            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener {
                        viewModel.createProfile(nameField.text.toString(), {
                            Toast.makeText(
                                requireContext(),
                                "Пользователь создан",
                                Toast.LENGTH_SHORT
                            ).show()

                            dialog.dismiss()
                        }, {
                            Toast.makeText(
                                requireContext(),
                                "Профиль с таким названием уже существует",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                    }
            }

            dialog.show()

        }
        
        btnDeleteProfile.setOnClickListener {
            val alertDialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            alertDialog.setCancelable(false)
            alertDialog.setTitle("Внимание")
            alertDialog.setMessage("Вы уверены, что хотите удалить профиль ${currentProfile?.name}?")
            alertDialog.setPositiveButton("Да") { dialog: DialogInterface, which: Int ->
                currentProfile?.let { profile ->
                    viewModel.deleteProfile(profile)

                }
            }
            alertDialog.setNegativeButton("Отмены") { dialog: DialogInterface?, which: Int ->

            }
            alertDialog.show()
        }


        val back = view.findViewById<ImageView>(R.id.iv_base_settings_back)
        back.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.nav_host_fragment, MainFragment())?.commit()
        }


    }


}