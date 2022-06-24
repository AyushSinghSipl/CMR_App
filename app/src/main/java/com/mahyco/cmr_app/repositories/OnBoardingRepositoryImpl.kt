package com.mahyco.cmr_app.repositories

import com.google.gson.JsonObject
import com.mahyco.cmr_app.api.IOnBoardingService
import com.mahyco.cmr_app.core.Constant
import com.mahyco.cmr_app.model.BaseResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnBoardingRepositoryImpl(private val ionBoardingService: IOnBoardingService) :
        IOnBoardingRepository {
    override fun getToken(type: String, userName: String, sapcode: String, pwd: String): Observable<BaseResponse> {
        return Observable.create {
            val apiCall = ionBoardingService.postGetToken(type, userName, sapcode, pwd)

            apiCall.enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    val response = BaseResponse()
                    response.errorCodeData = Constant.FAILED_STATUS
                    response.errorMessageData = t.localizedMessage
                    it.onNext(response)
                    it.onComplete()
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    when {
                        response.isSuccessful -> {
                            var successResponse = BaseResponse()
                            successResponse.errorCodeData = Constant.SUCCESS_STATUS
                            successResponse.jsonObjectData = response.body()
                            it.onNext(successResponse)
                            it.onComplete()
                        }
                        else -> {
                            var errorResponse = BaseResponse()
                            errorResponse.errorCodeData = "UNKNOWN_ERROR"
                            errorResponse.errorMessageData =
                                    "We are unable to process your request at this time."
                            it.onNext(errorResponse)
                            it.onComplete()
                        }
                    }
                }
            })
        }
    }
}