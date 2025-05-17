package com.dicoding.final_submission.ui.finished

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.final_submission.R
import com.dicoding.final_submission.database.Favorite
import com.dicoding.final_submission.databinding.ActivityDetailFinishedBinding
import com.dicoding.final_submission.helper.DetailFinishedViewModelFactory
import com.dicoding.final_submission.response.EventResponse
import com.dicoding.final_submission.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailFinished : AppCompatActivity() {

    private lateinit var binding: ActivityDetailFinishedBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: DetailFinishedViewModel
    private val detailFinishedViewModel: DetailFinishedViewModel by viewModels()

    companion object {
        const val PREF_NAME = "favorite_preferences_finished"
        const val EVENT_ID = "EVENT_ID"
    }

    private var eventId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFinishedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = DetailFinishedViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(DetailFinishedViewModel::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        eventId = intent.getIntExtra(EVENT_ID, -1)
        Log.d("DetailFinished", "Event ID: $eventId")

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        supportActionBar?.hide()

        // Hide or disable fabShare and fabFavorite initially
        binding.fabShare.hide()
        binding.fabShare.isEnabled = false

        setupFavoriteButton()
        getEventDetails(eventId)
        setupFloatingActionButton()
    }

    private fun setupFavoriteButton() {
        detailFinishedViewModel.getFavById(eventId.toString()).observe(this) { favorite ->
            val isFavorite = favorite != null
            updateFavoriteIcon(isFavorite)

            binding.fabShare.setOnClickListener {
                if (isFavorite) {
                    favorite?.let { detailFinishedViewModel.delete(it) }
                    Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
                } else {
                    val newFavorite = Favorite(
                        eventId.toString(),
                        binding.homeEventNameFinish.text.toString(),
                        binding.homeEventLogoFinished.drawable.toString()
                    )
                    detailFinishedViewModel.insert(newFavorite)
                    Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
                }
                updateFavoriteIcon(!isFavorite)
            }
        }
    }

    private fun shareEventDetails() {
        val eventName = binding.homeEventNameFinish.text.toString()
        val shareText = "Check out this event: $eventName"
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, null))
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding.fabShare.setImageResource(R.drawable.ic_favorite)
        } else {
            binding.fabShare.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    private fun getEventDetails(eventId: Int) {
        binding.progressBarHomeFin.visibility = android.view.View.VISIBLE
        val apiService = ApiConfig.getApiService()
        apiService.getFinishedEvent().enqueue(object : Callback<EventResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                binding.progressBarHomeFin.visibility = android.view.View.GONE
                if (response.isSuccessful) {
                    val event = response.body()?.listEvents?.find { it.id == eventId }
                    event?.let { eventDetail ->

                        binding.homeEventLogoFinished.loadImage(eventDetail.mediaCover)

                        binding.homeEventNameFinish.text = eventDetail.name
                        binding.homeEventOwnerFinish.text = eventDetail.ownerName
                        binding.homeEventTimeFinish.text =
                            "${eventDetail.beginTime} - ${eventDetail.endTime}"
                        binding.homeEventQuotaFinish.text = getString(
                            R.string.quota_text,
                            eventDetail.quota - eventDetail.registrants
                        )
                        binding.homeEventDescFinish.text = HtmlCompat.fromHtml(
                            eventDetail.description,
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )

                        binding.homeEventLinkButtonFinish.setOnClickListener {
                            if (eventDetail.link.isNotEmpty()) {
                                val eventUrl = Uri.parse(eventDetail.link)
                                val intent = Intent(Intent.ACTION_VIEW, eventUrl)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this@DetailFinished,
                                    getString(R.string.link_not_available),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }


                        binding.fabShare.show()
                        binding.fabShare.isEnabled = true
                    } ?: run {
                        Toast.makeText(this@DetailFinished, "Event not found", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        this@DetailFinished,
                        "Failed to load event details",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                binding.progressBarHomeFin.visibility = android.view.View.GONE
                Toast.makeText(this@DetailFinished, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
                t.printStackTrace()
            }
        })
    }

    private fun setupFloatingActionButton() {
        binding.fabShare.setOnClickListener {
            shareEventDetails()
        }
    }

    private fun ImageView.loadImage(url: String) {
        Glide.with(this.context)
            .load(url)
            .centerCrop()
            .into(this)
    }
}
