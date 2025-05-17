package com.dicoding.final_submission.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.final_submission.database.Favorite
import com.dicoding.final_submission.repository.FavoriteRepository

class DetailFavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)


    fun getFavById(id: String): LiveData<Favorite> {
        return mFavoriteRepository.getFavoriteById(id)
    }

    fun delete(favorite: Favorite) {
        mFavoriteRepository.delete(favorite)
    }
}