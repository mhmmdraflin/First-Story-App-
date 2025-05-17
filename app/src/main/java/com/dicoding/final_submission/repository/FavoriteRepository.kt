package com.dicoding.final_submission.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.final_submission.database.Favorite
import com.dicoding.final_submission.database.FavoriteDao
import com.dicoding.final_submission.database.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val myFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadScheduledExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        myFavoriteDao = db.favDao()
    }

    fun getAllFavorites(): LiveData<List<Favorite>> = myFavoriteDao.getAllFavorites()


    fun insert(favorite: Favorite) {
        executorService.execute { myFavoriteDao.insert(favorite) }
    }

    fun delete(favorite: Favorite) {
        executorService.execute { myFavoriteDao.delete(favorite) }
    }

    fun getFavoriteById(id: String): LiveData<Favorite> {
        return myFavoriteDao.getFavoriteEventById(id)
    }

}