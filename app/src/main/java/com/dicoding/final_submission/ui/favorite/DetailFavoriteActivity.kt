package com.dicoding.final_submission.ui.favorite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.final_submission.R
import com.dicoding.final_submission.database.Favorite
import com.dicoding.final_submission.databinding.ActivityDetailFavoriteBinding
import com.dicoding.final_submission.helper.FavoriteViewModelFactory
import com.dicoding.final_submission.retrofit.ApiConfig
import com.dicoding.final_submission.ui.finished.loadImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailFavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailFavoriteBinding
    private lateinit var detailFavoriteViewModel: DetailFavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailFavoriteViewModel =
            ViewModelProvider(this, FavoriteViewModelFactory.getInstance(application)).get(
                DetailFavoriteViewModel::class.java
            )

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val eventId = intent.getIntExtra("EVENT_ID", 0)
        getDetails(eventId)

        binding.uncheck.setOnClickListener {
            remove(eventId)
        }

        checkFavorite(eventId)
    }

    private fun checkFavorite(eventId: Int) {
        detailFavoriteViewModel.getFavById(eventId.toString()).observe(this) { favorite ->
            if (favorite != null) {
                binding.favButton.visibility = View.GONE
                binding.uncheck.visibility = View.VISIBLE
            } else {
                binding.favButton.visibility = View.VISIBLE
                binding.uncheck.visibility = View.GONE
            }
        }
    }

    private fun remove(eventId: Int) {
        val favorite = Favorite(
            id = eventId.toString(),
            name = binding.detailName.text.toString(),
            mediaCover = null
        )
        detailFavoriteViewModel.delete(favorite)
        binding.favButton.visibility = View.GONE
        binding.uncheck.visibility = View.VISIBLE
    }

    private fun getDetails(eventId: Int) {

        binding.favButton.isEnabled = false
        binding.uncheck.isEnabled = false
        binding.favButton.visibility = View.GONE
        binding.uncheck.visibility = View.GONE
        binding.progresBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val response = withContext(Dispatchers.IO) {
                    apiService.getFavoriteDetails().execute()
                }
                binding.progresBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val event = response.body()?.listEvents?.find { it.id == eventId }
                    event?.let {
                        binding.detailLogo.loadImage(it.mediaCover)
                        binding.detailName.text = it.name
                        binding.detailOwner.text = it.ownerName
                        binding.detailTime.text = "${it.beginTime} - ${it.endTime}"
                        binding.detailQuota.text =
                            "Jumlah kuota acara yang masih tersedia: ${it.quota - it.registrants}"
                        binding.detailEventDescription.text =
                            HtmlCompat.fromHtml(it.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
                        val eventLink = it.link

                        binding.detailLinkButton.setOnClickListener {
                            if (eventLink.isNotEmpty()) {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(eventLink))
                                startActivity(intent)
                            }
                        }

                        binding.favButton.isEnabled = true
                        binding.uncheck.isEnabled = true
                        binding.favButton.visibility = View.VISIBLE
                        binding.uncheck.visibility = View.VISIBLE
                    }
                }
            } catch (e: Exception) {
                binding.progresBar.visibility = View.GONE
                e.printStackTrace()
            }
        }
    }
}