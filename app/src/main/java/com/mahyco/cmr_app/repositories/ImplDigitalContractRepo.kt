package com.mahyco.cmr_app.repositories

import com.google.gson.JsonObject
import com.mahyco.cmr_app.api.IDataServiceCMR
import com.mahyco.cmr_app.model.getCMRDataForValidation.GetCMRDataForValidResp
import com.mahyco.cmr_app.model.getProductListResponse.GetProductList
import com.mahyco.cmr_app.model.getSeasonListResponse.GetSeasonsResponse
import com.mahyco.cmr_app.model.getVendorListResponse.VendorListResponse
import com.mahyco.cmr_app.model.getcenterlistresponse.GetCenterListResponse
import com.mahyco.cmr_app.model.vendordetailsforcontract.VDContractResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImplDigitalContractRepo(private val iDataServiceCMR: IDataServiceCMR) :
    DigitalContractRepo {

    override fun getGetSeasonList(): Observable<GetSeasonsResponse> {
        return Observable.create {
            val apiCall = iDataServiceCMR.getSeasonList()

            apiCall.enqueue(object : Callback<GetSeasonsResponse> {
                override fun onFailure(call: Call<GetSeasonsResponse>, t: Throwable) {
                    val response = GetSeasonsResponse()
                    it.onNext(response)
                    it.onComplete()
                }

                override fun onResponse(
                    call: Call<GetSeasonsResponse>,
                    response: Response<GetSeasonsResponse>
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
                            val response = GetSeasonsResponse()
                            response.success = false
                            it.onNext(response)
                            it.onComplete()
                        }
                    }
                }
            })
        }
    }

    override fun getCenterList(userCode: String): Observable<GetCenterListResponse> {
        return Observable.create {
            val apiCall = iDataServiceCMR.getCenterList(userCode)

            apiCall.enqueue(object : Callback<GetCenterListResponse> {
                override fun onFailure(call: Call<GetCenterListResponse>, t: Throwable) {
                    val response = GetCenterListResponse()
                    it.onNext(response)
                    it.onComplete()
                }

                override fun onResponse(
                    call: Call<GetCenterListResponse>,
                    response: Response<GetCenterListResponse>
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
                            val response = GetCenterListResponse()
                            response.success = false
                            it.onNext(response)
                            it.onComplete()
                        }
                    }
                }
            })
        }
    }


    override fun postGetVendorList(
        token: String,
        jsonObject: JsonObject
    ): Observable<VendorListResponse> {
        return Observable.create {
            val apiCall = iDataServiceCMR.getVendorList(jsonObject)

            apiCall.enqueue(object : Callback<VendorListResponse> {
                override fun onFailure(call: Call<VendorListResponse>, t: Throwable) {
                    val response = VendorListResponse()
                    it.onNext(response)
                    it.onComplete()
                }

                override fun onResponse(
                    call: Call<VendorListResponse>,
                    response: Response<VendorListResponse>
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
                            val response = VendorListResponse()
                            response.success = false
                            it.onNext(response)
                            it.onComplete()
                        }
                    }
                }
            })
        }
    }

    override fun postGetProductList(
        token: String,
        jsonObject: JsonObject
    ): Observable<GetProductList> {
        return Observable.create {
            val apiCall = iDataServiceCMR.getProductList(jsonObject)

            apiCall.enqueue(object : Callback<GetProductList> {
                override fun onFailure(call: Call<GetProductList>, t: Throwable) {
                    val response = GetProductList()
                    it.onNext(response)
                    it.onComplete()
                }

                override fun onResponse(
                    call: Call<GetProductList>,
                    response: Response<GetProductList>
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
                            val response = GetProductList()
                            response.success = false
                            it.onNext(response)
                            it.onComplete()
                        }
                    }
                }
            })
        }
    }

    override fun postGetVendorDetailsForContract(
        token: String,
        jsonObject: JsonObject
    ): Observable<VDContractResponse> {
        return Observable.create {
            val apiCall = iDataServiceCMR.getVendorDetailsForContract(jsonObject)

            apiCall.enqueue(object : Callback<VDContractResponse> {
                override fun onFailure(call: Call<VDContractResponse>, t: Throwable) {
                    val response = VDContractResponse()
                    it.onNext(response)
                    it.onComplete()
                }

                override fun onResponse(
                    call: Call<VDContractResponse>,
                    response: Response<VDContractResponse>
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
                            val response = VDContractResponse()
                            response.success = false
                            it.onNext(response)
                            it.onComplete()
                        }
                    }
                }
            })
        }
    }

}