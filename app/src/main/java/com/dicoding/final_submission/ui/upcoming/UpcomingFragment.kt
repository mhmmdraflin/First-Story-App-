package com.dicoding.final_submission.ui.upcoming

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.final_submission.R
import com.dicoding.final_submission.databinding.FragmentUpcomingBinding
import com.dicoding.final_submission.response.EventResponse
import com.dicoding.final_submission.response.ListEventsItem
import com.dicoding.final_submission.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterEvent: FirstAdapter
    private var dataList: List<ListEventsItem> = ArrayList()
    private val viewModel: UpcomingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)

        Log.d("UpcomingFragment", "Initializing adapterEvent")

        adapterEvent = FirstAdapter(requireContext(), dataList) { eventId ->
            val intent = Intent(context, DetailUpcoming::class.java)
            intent.putExtra(DetailUpcoming.EVENT_ID, eventId)
            startActivity(intent)
        }

        binding.recyclerViewUpcoming.adapter = adapterEvent
        binding.recyclerViewUpcoming.layoutManager = LinearLayoutManager(context)

        val dividerItemDecoration = DividerItemDecoration(
            binding.recyclerViewUpcoming.context,
            LinearLayoutManager.VERTICAL
        )
        binding.recyclerViewUpcoming.addItemDecoration(dividerItemDecoration)

        observeViewModel()
        getUpcomingEvents()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.events.observe(viewLifecycleOwner) { events ->
            adapterEvent.updateData(events)
            if (events.isEmpty()) {
                showToast(getString(R.string.no_events_available))
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isAdded) {
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.recyclerViewUpcoming.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    private fun showToast(message: String) {
        if (isAdded) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUpcomingEvents() {
        if (!isAdded) return
        showLoading(true)
        val apiService = ApiConfig.getApiService()
        apiService.getUpcomingEvents(1).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (!isAdded) return
                showLoading(false)
                if (response.isSuccessful) {
                    val eventResponse = response.body()
                    Log.d("UpcomingFragment", "Response: ${eventResponse?.listEvents}")

                    if (eventResponse != null && eventResponse.listEvents.isNotEmpty()) {
                        // Filter the upcoming events if needed
                        dataList = eventResponse.listEvents
                        adapterEvent.updateData(dataList)
                    } else {
                        showToast(getString(R.string.no_events_available))
                    }
                } else {
                    Log.e("UpcomingFragment", "Error: ${response.message()}")
                    showToast(getString(R.string.failed_to_load_events, response.message()))
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                if (!isAdded) return
                showLoading(false)
                Log.e("UpcomingFragment", "Failure: ${t.message}")
                showToast(getString(R.string.error_loading_events, t.message))
            }
        })
    }
}