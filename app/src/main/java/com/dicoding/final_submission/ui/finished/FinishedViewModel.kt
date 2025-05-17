package com.dicoding.final_submission.ui.finished

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.final_submission.response.EventResponse
import com.dicoding.final_submission.response.ListEventsItem
import com.dicoding.final_submission.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> get() = _events

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    init {
        fetchFinishedEvents()
    }

    private fun fetchFinishedEvents() {
        _loading.value = true
        val apiService = ApiConfig.getApiService()
        apiService.getFinishedEvent().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _loading.value = false
                if (response.isSuccessful) {
                    val eventResponse = response.body()
                    if (eventResponse != null && eventResponse.listEvents.isNotEmpty()) {
                        _events.value = eventResponse.listEvents
                    } else {
                        _events.value = emptyList() // Tidak ada data
                    }
                } else {
                    Log.e("FinishedViewModel", "Failed to load events: ${response.message()}")
                    _events.value = emptyList() // Handle error
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _loading.value = false
                Log.e("FinishedViewModel", "Error loading events: ${t.message}")
                _events.value = emptyList() // Handle error
            }
        })
    }
}
