package com.dicoding.final_submission.ui.setting

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.final_submission.databinding.FragmentSettingBinding
import com.dicoding.final_submission.theme.ThemePreference

val Context.dataStore by preferencesDataStore(name = "settings")

class SettingFragment : Fragment() {

    private lateinit var settingsViewModel: SettingViewModel
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private var isFirstLoad = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        val themePreference = ThemePreference(requireContext().dataStore)
        val factory = SettingsViewModelFactory(themePreference)
        settingsViewModel = ViewModelProvider(this, factory).get(SettingViewModel::class.java)

        settingsViewModel.isDarkMode.observe(viewLifecycleOwner) { isDarkMode ->
            if (isFirstLoad) {
                binding.switchDarkMode.isChecked = isDarkMode
                isFirstLoad = false
            } else {
                applyDarkMode(isDarkMode)
            }
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.setDarkMode(isChecked)
            applyDarkMode(isChecked)
        }

        return binding.root
    }

    private fun applyDarkMode(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun setupDailyReminder(context: Context, isChecked: Boolean) {
    if (isChecked) {
    } else {

    }
}

class SettingsViewModelFactory(private val themePreference: ThemePreference) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingViewModel(themePreference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}