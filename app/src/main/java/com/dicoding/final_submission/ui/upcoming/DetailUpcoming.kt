package com.dicoding.final_submission.ui.upcoming

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.final_submission.R
import com.dicoding.final_submission.database.Favorite
import com.dicoding.final_submission.databinding.ActivityDetailUpcomingBinding
import com.dicoding.final_submission.helper.DetailUpcomingViewModelFactory
import com.dicoding.final_submission.response.EventResponse
import com.dicoding.final_submission.response.ListEventsItem
import com.dicoding.final_submission.retrofit.ApiConfig
import com.dicoding.final_submission.ui.finished.loadImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUpcoming : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUpcomingBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var eventId: Int = -1
    private var eventDetail: ListEventsItem? = null
    private lateinit var viewModel: DetailUpcomingViewModel

    private val detailUpcomingViewModel: DetailUpcomingViewModel by viewModels()

    companion object {
        const val PREF_NAME = "favorite_preferences"
        const val EVENT_ID = "EVENT_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUpcomingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = DetailUpcomingViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(DetailUpcomingViewModel::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        eventId = intent.getIntExtra(EVENT_ID, -1)
        if (eventId == -1) {
            Toast.makeText(this, "Invalid Event ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)

        Log.d("DetailActivity", "Event ID: $eventId")

        getUpcomingEvents()
        supportActionBar?.hide()

        setupFavoriteButton()
    }

    private fun getUpcomingEvents() {
        binding.fabShare.visibility = View.GONE
        binding.fabShare.visibility = View.GONE
        binding.progressBarHomeUpcoming.visibility = View.VISIBLE

        val apiService = ApiConfig.getApiService()

        apiService.getUpcomingEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                binding.progressBarHomeUpcoming.visibility = View.GONE
                if (response.isSuccessful) {
                    eventDetail = response.body()?.listEvents?.find { it.id == eventId }
                    eventDetail?.let {
                        updateUI(it)
                        binding.fabShare.visibility = View.VISIBLE
                        binding.fabShare.visibility = View.VISIBLE
                    } ?: showToast(getString(R.string.event_not_found))
                } else {
                    showToast(getString(R.string.failed_to_fetch_event_data))
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                binding.progressBarHomeUpcoming.visibility = View.GONE
                showToast(getString(R.string.error_occurred, t.message))
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(eventDetail: ListEventsItem) {
        binding.homeEventLogoUpcoming.loadImage(eventDetail.mediaCover)
        binding.homeEventNameUpcoming.text = eventDetail.name
        binding.homeEventOwnerUpcoming.text = eventDetail.ownerName
        binding.homeEventTimeUpcoming.text = "${eventDetail.beginTime} - ${eventDetail.endTime}"
        binding.homeEventQuotaUpcoming.text = getString(
            R.string.quota_text,
            eventDetail.quota - eventDetail.registrants
        )
        binding.homeEventDescUpcoming.text = HtmlCompat.fromHtml(
            eventDetail.description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        binding.homeEventLinkButtonUpcoming.setOnClickListener {
            val eventLink = eventDetail.link
            if (!eventLink.isNullOrEmpty() && eventLink.startsWith("http")) {
                val eventUrl = Uri.parse(eventLink)
                startActivity(Intent(Intent.ACTION_VIEW, eventUrl))
            } else {
                showToast(getString(R.string.link_not_available))
            }
        }
    }

    private fun setupFavoriteButton() {
        detailUpcomingViewModel.getFavById(eventId.toString()).observe(this) { favorite ->
            val isFavorite = favorite != null
            updateFavoriteIcon(isFavorite)

            binding.fabShare.setOnClickListener {
                if (isFavorite) {
                    favorite?.let { detailUpcomingViewModel.delete(it) }
                    Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
                } else {
                    eventDetail?.let {
                        val newFavorite = Favorite(
                            eventId.toString(),
                            it.name,
                            it.mediaCover
                        )
                        detailUpcomingViewModel.insert(newFavorite)
                        Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
                    }
                }
                updateFavoriteIcon(!isFavorite)
            }

            binding.fabShare.setOnLongClickListener {
                shareEventDetails()
                true
            }
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        binding.fabShare.setImageResource(
            if (isFavorite) R.drawable.ic_favorite
            else R.drawable.baseline_favorite_border_24
        )
    }

    private fun shareEventDetails() {
        val eventName = binding.homeEventNameUpcoming.text.toString()
        val shareText = "Check out this event: $eventName"
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, null))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}