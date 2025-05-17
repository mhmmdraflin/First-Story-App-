package com.dicoding.final_submission.ui.finished

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.final_submission.R
import com.dicoding.final_submission.databinding.FragmentFinishedBinding
import com.dicoding.final_submission.response.EventResponse
import com.dicoding.final_submission.response.ListEventsItem
import com.dicoding.final_submission.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterEvent: SecondAdapter
    private var dataList: List<ListEventsItem> = ArrayList()
    private lateinit var viewModel: FinishedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(FinishedViewModel::class.java)

        Log.d("FinishedFragment", "Initializing adapterEvent")

        adapterEvent = SecondAdapter(requireContext(), dataList)
        binding.recyclerViewFinished.adapter = adapterEvent
        binding.recyclerViewFinished.layoutManager = LinearLayoutManager(context)

        val dividerItemDecoration = DividerItemDecoration(
            binding.recyclerViewFinished.context,
            LinearLayoutManager.VERTICAL
        )
        binding.recyclerViewFinished.addItemDecoration(dividerItemDecoration)

        viewModel.events.observe(viewLifecycleOwner, Observer { events ->
            dataList = events // Menyimpan data awal dari API
            adapterEvent.updateData(events)
            if (events.isEmpty()) {
                showToast(getString(R.string.no_events_available))
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            showLoading(isLoading)
        })

        setupSearchView()

        getFinishedEvents()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isAdded) {
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.recyclerViewFinished.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    private fun showToast(message: String) {
        if (isAdded) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFinishedEvents() {
        showLoading(true)
        val apiService = ApiConfig.getApiService()
        apiService.getFinishedEvent().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val eventResponse = response.body()
                    if (eventResponse != null && eventResponse.listEvents.isNotEmpty()) {
                        dataList = eventResponse.listEvents // Simpan data awal ke dataList
                        adapterEvent.updateData(dataList)
                    } else {
                        showToast(getString(R.string.no_events_available))
                    }
                } else {
                    showToast(getString(R.string.failed_to_load_events, response.message()))
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showLoading(false)
                t.printStackTrace()
                showToast(getString(R.string.error_loading_events, t.message))
            }
        })
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                fetchEvents(newText)
                return true
            }
        })
    }

    private fun fetchEvents(query: String?) {
        // Jika tidak ada query, tampilkan semua data di dataList
        if (query.isNullOrEmpty()) {
            adapterEvent.updateData(dataList)
        } else {
            // Filter data lokal dari dataList berdasarkan query pencarian
            val filteredEvents = dataList.filter { event ->
                event.name.contains(query, ignoreCase = true) ||
                        event.description.contains(query, ignoreCase = true)
            }
            adapterEvent.updateData(filteredEvents)

            if (filteredEvents.isEmpty()) {
                showToast(getString(R.string.no_events_available))
            }
        }
    }
}
