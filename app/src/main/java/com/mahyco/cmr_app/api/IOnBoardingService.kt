package com.mahyco.cmr_app.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface IOnBoardingService {
    @FormUrlEncoded
    @Headers(value = ["Content-Type: application/x-www-form-urlencoded", "Accept: application/json"])
    @POST("token")
    fun postGetToken( @Field ("grant_type") type: String, @Field ("username") username: String,
                      @Field ("sapcode") sapcode: String, @Field ("password") password: String): Call<JsonObject>
}