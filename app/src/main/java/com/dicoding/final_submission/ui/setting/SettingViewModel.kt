package com.dicoding.final_submission.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.final_submission.theme.ThemePreference
import kotlinx.coroutines.launch

class SettingViewModel(private val themePreference: ThemePreference) : ViewModel() {

    val isDarkMode: LiveData<Boolean> = themePreference.isDarkMode.asLiveData()

    fun setDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            themePreference.setDarkMode(isDarkMode)
        }
    }
}
