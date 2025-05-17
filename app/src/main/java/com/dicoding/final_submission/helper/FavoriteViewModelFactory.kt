package com.dicoding.final_submission.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.final_submission.ui.favorite.DetailFavoriteViewModel
import com.dicoding.final_submission.ui.favorite.FavoriteViewModel
import com.dicoding.final_submission.ui.finished.DetailFinishedViewModel
import com.dicoding.final_submission.ui.upcoming.DetailUpcomingViewModel

class FavoriteViewModelFactory(private val mApplication: Application) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: FavoriteViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): FavoriteViewModelFactory {
            if (INSTANCE == null) {
                synchronized(FavoriteViewModelFactory::class.java) {
                    INSTANCE = FavoriteViewModelFactory(application)
                }
            }

            return INSTANCE as FavoriteViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(DetailUpcomingViewModel::class.java)) {
            return DetailUpcomingViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(DetailFinishedViewModel::class.java)) {
            return DetailFinishedViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(DetailFavoriteViewModel::class.java)) {
            return DetailFavoriteViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

}