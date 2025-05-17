package com.dicoding.final_submission.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.final_submission.response.ListEventsItem

class UpcomingViewModel : ViewModel() {

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> get() = _events

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun setEvents(eventList: List<ListEventsItem>) {
        _events.value = eventList
    }

    fun setLoading(isLoading: Boolean) {
        _loading.value = isLoading
    }
}
