package com.dicoding.final_submission.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.final_submission.databinding.FragmentFavoriteBinding
import com.dicoding.final_submission.helper.FavoriteViewModelFactory
import com.dicoding.final_submission.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var ViewModelFavorite: FavoriteViewModel
    private lateinit var Adapterfavorite: FavoriteAdapter
    private var favorite1: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val FavoriteFactory = FavoriteViewModelFactory(requireActivity().application)
        ViewModelFavorite =
            ViewModelProvider(this, FavoriteFactory).get(FavoriteViewModel::class.java)

        Adapterfavorite = FavoriteAdapter(requireContext(), emptyList())
        binding.favoriteRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.favoriteRecycler.adapter = Adapterfavorite

        ViewModelFavorite.getAllFavorites().observe(viewLifecycleOwner) { favorites ->
            favorite1 = favorites.map { it.id }
            getFavorite()
        }
    }

    private fun getFavorite() {
        binding.progressbarFavorite.visibility = View.VISIBLE
        val apiService = ApiConfig.getApiService()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getAllEvents()

                val favoriteEvents = response.listEvents.filter { event ->
                    favorite1.contains(event.id.toString())
                }

                withContext(Dispatchers.Main) {
                    binding.progressbarFavorite.visibility = View.GONE
                    Adapterfavorite.updateData(favoriteEvents)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.progressbarFavorite.visibility = View.GONE
                }
            }
        }
    }
}