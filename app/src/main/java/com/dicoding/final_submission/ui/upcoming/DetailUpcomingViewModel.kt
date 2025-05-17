package com.dicoding.final_submission.ui.upcoming

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.final_submission.database.Favorite
import com.dicoding.final_submission.repository.FavoriteRepository

class DetailUpcomingViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getFavById(id: String): LiveData<Favorite> {
        return mFavoriteRepository.getFavoriteById(id)
    }

    fun insert(favorite: Favorite) {
        mFavoriteRepository.insert(favorite)
    }

    fun delete(favorite: Favorite) {
        mFavoriteRepository.delete(favorite)
    }

}