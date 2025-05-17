package com.dicoding.final_submission.retrofit

import com.dicoding.final_submission.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("events")
    fun getUpcomingEvents(
        @Query("active") active: Int = 1
    ): Call<EventResponse>

    @GET("events?active=0")
    fun getFinishedEvent():
            Call<EventResponse>

    @GET("events")
    suspend fun getAllEvents(): EventResponse

    @GET("events")
    fun getFavoriteDetails(): Call<EventResponse>


}