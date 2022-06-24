package com.mahyco.cmr_app.repositories

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.mahyco.cmr_app.api.IDataServiceCMR
import com.mahyco.cmr_app.model.getActivityType.GetActivityTypeResponseItem
import com.mahyco.cmr_app.model.getCMRDataForValidation.GetCMRDataForValidResp
import com.mahyco.cmr_app.model.getCMRFortyDataResp.GetCMRFortyDataResp
import com.mahyco.cmr_app.model.getCMRTenDataResp.GetCMRTenDataResp
import com.mahyco.cmr_app.model.getCMRThirtyDataResp.GetCMRThirtyDataResp
import com.mahyco.cmr_app.model.getCMRTwentyDataResp.GetCMRTwentyDataResp
import com.mahyco.cmr_app.model.getEventTourData.GetTourEventResponse
import com.mahyco.cmr_app.model.getEventTourData.TourEventParamItem
import com.mahyco.cmr_app.model.getMoStaffwiserGrowerReport.GetMoStaffWiserGrowerReport
import com.mahyco.cmr_app.model.getVehicleTypeResponse.GetVehicleTypeResponse
import com.mahyco.cmr_app.model.getVehicleTypeResponse.GetVehicleTypeResponseItem
import com.mahyco.cmr_app.model.login.LoginParam
import com.mahyco.cmr_app.model.login.LoginResponseModel
import io.reactivex.Observable
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ImplCMRDataRepo(private val iDataServiceCMR: IDataServiceCMR) :
    CMRDataRepo {

    override fun postGetCMRDataForValAPI(
        token: String, jsonObject: JsonObject
    ): Observable<GetCMRDataForValidResp> {
        return Observable.create {
            val apiCall = iDataServiceCMR.postGetCMRDataForValidation(token, jsonObject)

            apiCall.enqueue(object : Callback<GetCMRDataForValidResp> {
                override fun onFailure(call: Call<GetCMRDataForValidResp>, t: Throwable) {
                    val response = GetCMRDataForValidResp()
                    it.onNext(response)
                    it.onComplete()
                }

                override fun onResponse(
                    call: Call<GetCMRDataForValidResp>,
                    response: Response<GetCMRDataForValidResp>
                ) {
                    when {
                        response.isSuccessful -> {
                            val responseData = response.body()
                            if (responseData != null) {
                                it.onNext(responseData)
                                it.onComplete()
                            }
                        }
                        else -> {
                            val response = GetCMRDataForValidResp()
                            response.success = false
                            it.onNext(response)
                            it.onComplete()
                        }
                    }
                }
            })
        }
    }


    override fun callLoginAPi(
        loginParam: LoginParam
    ): Observable<LoginResponseModel> {
        return Observable.create {
            val apiCall = iDataServiceCMR.callLogin(loginParam)

            apiCall.enqueue(object : Callback<LoginResponseModel> {
                override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                    val response = LoginResponseModel()
                    it.onNext(response)
                    it.onComplete()
                }

                override fun onResponse(
                    call: Call<LoginResponseModel>,
                    response: Response<LoginResponseModel>
                ) {
                    when {
                        response.isSuccessful -> {
                            val responseData = response.body()
                            if (responseData != null) {
                                it.onNext(responseData)
                                it.onComplete()
                            }
                        }
                        else -> {
                            val response = LoginResponseModel()
                           // response.success = false
                            it.onNext(response)
                            it.onComplete()
                        }
                    }
                }
            })
        }
    }

    override fun getVehicleList(): Observable<List<GetVehicleTypeResponseItem>> {
        return Observable.create {
            val apiCall = iDataServiceCMR.getVehicleType()

            apiCall.enqueue(object : Callback<List<GetVehicleTypeResponseItem>> {
                override fun onFailure(call: Call<List<GetVehicleTypeResponseItem>>, t: Throwable) {
                  /*  val gson = Gson()
                    val collectionType: Type =
                        object : TypeToken<Collection<GetVehicleTypeResponseItem?>?>() {}.type
                    val enums: Collection<GetVehicleTypeResponseItem> =
                        gson.fromJson(yourJson, collectionType)*/
                  //  val response = call
                    //it.onNext(response)
                    it.onComplete()
                }

                override fun onResponse(
                    call: Call<List<GetVehicleTypeResponseItem>>,
                    response: Response<List<GetVehicleTypeResponseItem>>
                ) {
                    when {
                        response.isSuccessful -> {
                            val responseData = response.body()
                            if (responseData != null) {
                                it.onNext(responseData)
                                it.onComplete()
                            }
                        }
                        else -> {
                            //val response = List<GetVehicleTypeResponseItem>()
//                            response.success = false
                        //    it.onNext(response)
                            it.onComplete()
                        }
                    }
                }
            })
        }
    }

    override fun getActivityList(): Observable<List<GetActivityTypeResponseItem>> {
        return Observable.create {
            val apiCall = iDataServiceCMR.getActivityType()

            apiCall.enqueue(object : Callback<List<GetActivityTypeResponseItem>> {
                override fun onFailure(call: Call<List<GetActivityTypeResponseItem>>, t: Throwable) {
                    /*  val gson = Gson()
                      val collectionType: Type =
                          object : TypeToken<Collection<GetVehicleTypeResponseItem?>?>() {}.type
                      val enums: Collection<GetVehicleTypeResponseItem> =
                          gson.fromJson(yourJson, collectionType)*/
                    //  val response = call
                    //it.onNext(response)
                    it.onComplete()
                }

                override fun onResponse(
                    call: Call<List<GetActivityTypeResponseItem>>,
                    response: Response<List<GetActivityTypeResponseItem>>
                ) {
                    when {
                        response.isSuccessful -> {
                            val responseData = response.body()
                            if (responseData != null) {
                                it.onNext(responseData)
                                it.onComplete()
                            }
                        }
                        else -> {
                            //val response = List<GetVehicleTypeResponseItem>()
//                            response.success = false
                            //    it.onNext(response)
                            it.onComplete()
                        }
                    }
                }
            })
        }
    }




    override fun postGetCMRTenData(
        token: String,
        jsonObject: JsonObject
    ): Observable<GetCMRTenDataResp> {
        return Observable.create {
            val apiCall = iDataServiceCMR.postGetCMRTenData(token, jsonObject)

            apiCall.enqueue(object : Callback<GetCMRTenDataResp> {
                override fun onFailure(call: Call<GetCMRTenDataResp>, t: Throwable) {
                    val response = GetCMRTenDataResp()
                    it.onNext(response)
                    it.onComplete()
                }

                override fun onResponse(
                    call: Call<GetCMRTenDataResp>,
                    response: Response<GetCMRTenDataResp>
                ) {
                    when {
                        response.isSuccessful -> {
                            val responseData = response.body()
                            if (responseData != null) {
                                it.onNext(responseData)
                                it.onComplete()
                            }
                        }
                        else -> {
                            val response = GetCMRTenDataResp()
                            response.success = false
                            it.onNext(response)
                            it.onComplete()
                        }
                    }
                }
            })
        }
    }

    override fun postTourEventData(list: List<TourEventParamItem>): Observable<GetTourEventResponse> {
        return Observable.create {
            val apiCall = iDataServiceCMR.uploadTourData( list)

            apiCall.enqueue(object : Callback<GetTourEventResponse> {
                override fun onFailure(call: Call<GetTourEventResponse>, t: Throwable) {
                    val response = GetTourEventResponse()
                    it.onNext(response)
                    it.onComplete()
                }

                override fun onResponse(
                    call: Call<GetTourEventResponse>,
                    response: Response<GetTourEventResponse>
                ) {
                    when {
                        response.isSuccessful -> {
                            val responseData = response.body()
                            if (responseData != null) {
                                it.onNext(responseData)
                                it.onComplete()
                            }
                        }
                        else -> {
                            val response = GetTourEventResponse()
                         //   response.success = false
                           // it.onNext(response)
                            it.onComplete()
                        }
                    }
                }
            })
        }
    }

    override fun realtimetourdata(list: List<TourEventParamItem>): Observable<GetTourEventResponse> {
        return Observable.create {
            val apiCall = iDataServiceCMR.realtimetourdata( list)

            apiCall.enqueue(object : Callback<GetTourEventResponse> {
                override fun onFailure(call: Call<GetTourEventResponse>, t: Throwable) {
                    val response = GetTourEventResponse()
                    it.onNext(response)
                    it.onComplete()
                }

                override fun onResponse(
                    call: Call<GetTourEventResponse>,
                    response: Response<GetTourEventResponse>
                ) {
                    when {
                        response.isSuccessful -> {
                            val responseData = response.body()
                            if (responseData != null) {
                                it.onNext(responseData)
                                it.onComplete()
                            }
                        }
                        else -> {
                            val response = GetTourEventResponse()
                            //   response.success = false
                            // it.onNext(response)
                            it.onComplete()
                        }
                    }
                }
            })
        }
    }

    override fun postGetCMRTwentyData(
        token: String,
        jsonObject: JsonObject
    ): Observable<GetCMRTwentyDataResp> {
        return Observable.create {
            val apiCall = iDataServiceCMR.postGetCMRTwentyData(token, jsonObject)

            apiCall.enqueue(object : Callback<GetCMRTwentyDataResp> {
                override fun onFailure(call: Call<GetCMRTwentyDataResp>, t: Throwable) {
                    val response = GetCMRTwentyDataResp()
                    it.onNext(response)
                    it.onComplete()
                }

                override fun onResponse(
                    call: Call<GetCMRTwentyDataResp>,
                    response: Response<GetCMRTwentyDataResp>
                ) {
                    when {
                        response.isSuccessful -> {
                            val responseData = response.body()
                            if (responseData != null) {
                                it.onNext(responseData)
                                it.onComplete()
                            }
                        }
                        else -> {
                            val response = GetCMRTwentyDataResp()
                            response.success = false
                            it.onNext(response)
                            it.onComplete()
                        }
                    }
                }
            })
        }
    }

    override fun postGetCMRThirtyData(
        token: String,
        jsonObject: JsonObject
    ): Observable<GetCMRThirtyDataResp> {
        return Observable.create {
            val apiCall = iDataServiceCMR.postGetCMRThirtyData(token, jsonObject)

            apiCall.enqueue(object : Callback<GetCMRThirtyDataResp> {
                override fun onFailure(call: Call<GetCMRThirtyDataResp>, t: Throwable) {
                    val response = GetCMRThirtyDataResp()
                    it.onNext(response)
                    it.onComplete()
                }

                override fun onResponse(
                    call: Call<GetCMRThirtyDataResp>,
                    response: Response<GetCMRThirtyDataResp>
                ) {
                    when {
                        response.isSuccessful -> {
                            val responseData = response.body()
                            if (responseData != null) {
                                it.onNext(responseData)
                                it.onComplete()
                            }
                        }
                        else -> {
                            val response = GetCMRThirtyDataResp()
                            response.success = false
                            it.onNext(response)
                            it.onComplete()
                        }
                    }
                }
            })
        }
    }

    override fun postGetCMRFortyData(
        token: String,
        jsonObject: JsonObject
    ): Observable<GetCMRFortyDataResp> {
        return Observable.create {
            val apiCall = iDataServiceCMR.postGetCMRFortyData(token, jsonObject)

            apiCall.enqueue(object : Callback<GetCMRFortyDataResp> {
                override fun onFailure(call: Call<GetCMRFortyDataResp>, t: Throwable) {
                    val response = GetCMRFortyDataResp()
                    it.onNext(response)
                    it.onComplete()
                }

                override fun onResponse(
                    call: Call<GetCMRFortyDataResp>,
                    response: Response<GetCMRFortyDataResp>
                ) {
                    when {
                        response.isSuccessful -> {
                            val responseData = response.body()
                            if (responseData != null) {
                                it.onNext(responseData)
                                it.onComplete()
                            }
                        }
                        else -> {
                            val response = GetCMRFortyDataResp()
                            response.success = false
                            it.onNext(response)
                            it.onComplete()
                        }
                    }
                }
            })
        }
    }

    override fun postMoStaffWiserGrowerReport(
        token: String,
        jsonObject: JsonObject
    ): Observable<GetMoStaffWiserGrowerReport> {
        return Observable.create {
            val apiCall = iDataServiceCMR.postMoStaffWiserGrowerReport(token, jsonObject)

            apiCall.enqueue(object : Callback<GetMoStaffWiserGrowerReport> {
                override fun onFailure(call: Call<GetMoStaffWiserGrowerReport>, t: Throwable) {
                    val response = GetMoStaffWiserGrowerReport()
                    it.onNext(response)
                    it.onComplete()
                }

                override fun onResponse(
                    call: Call<GetMoStaffWiserGrowerReport>,
                    response: Response<GetMoStaffWiserGrowerReport>
                ) {
                    when {
                        response.isSuccessful -> {
                            val responseData = response.body()
                            if (responseData != null) {
                                it.onNext(responseData)
                                it.onComplete()
                            }
                        }
                        else -> {
                            val response = GetMoStaffWiserGrowerReport()
                            response.success = false
                            it.onNext(response)
                            it.onComplete()
                        }
                    }
                }
            })
        }    }
}