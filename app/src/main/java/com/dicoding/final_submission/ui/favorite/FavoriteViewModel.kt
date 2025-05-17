package com.dicoding.final_submission.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.final_submission.database.Favorite
import com.dicoding.final_submission.repository.FavoriteRepository

class FavoriteViewModel (application: Application) : AndroidViewModel(application) {
    private val repository: FavoriteRepository = FavoriteRepository(application)

    fun getAllFavorites(): LiveData<List<Favorite>> {
        return repository.getAllFavorites()
    }
}