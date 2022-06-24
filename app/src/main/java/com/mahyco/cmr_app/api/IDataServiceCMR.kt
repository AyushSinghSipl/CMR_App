package com.mahyco.cmr_app.api

import com.google.gson.JsonObject
import com.mahyco.cmr_app.model.getActivityType.GetActivityTypeResponseItem
import com.mahyco.cmr_app.model.getCMRDataForValidation.GetCMRDataForValidResp
import com.mahyco.cmr_app.model.getCMRFortyDataResp.GetCMRFortyDataResp
import com.mahyco.cmr_app.model.getProductListResponse.GetProductList
import com.mahyco.cmr_app.model.getSeasonListResponse.GetSeasonsResponse
import com.mahyco.cmr_app.model.getVendorListResponse.VendorListResponse
import com.mahyco.cmr_app.model.getcenterlistresponse.GetCenterListResponse
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
import com.mahyco.cmr_app.model.vendordetailsforcontract.VDContractResponse
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface IDataServiceCMR {

    @GET("api/digitalcontract/getSeasonList")
    fun getSeasonList(): Call<GetSeasonsResponse>

    @GET("api/VehicleType")
    fun getVehicleType(): Call<List<GetVehicleTypeResponseItem>>

    @POST("api/User")
    fun callLogin(@Body loginParam: LoginParam): Call<LoginResponseModel>

    @GET("api/ActivityType")
    fun getActivityType(): Call<List<GetActivityTypeResponseItem>>



    @POST("api/tour/upploadtourdata")
    fun uploadTourData(@Body list: List<TourEventParamItem>): Call<GetTourEventResponse>

    @POST("api/tour/realtimetourdata")
    fun realtimetourdata(@Body list: List<TourEventParamItem>): Call<GetTourEventResponse>


    @POST("api/digitalcontract/CenterListGet")
    fun getCenterList(@Query("usercode") userCode:String): Call<GetCenterListResponse>

    @POST("api/digitalcontract/VendorListGet")
    fun getVendorList(@Body json: JsonObject): Call<VendorListResponse>

    @POST("api/digitalcontract/ProductListGet")
    fun getProductList(@Body json: JsonObject): Call<GetProductList>

    @POST("api/digitalcontract/VendorDetailsForContractGet")
    fun getVendorDetailsForContract(@Body json: JsonObject): Call<VDContractResponse>

    @POST("api/cmr/getCMRDataForValidation")
    fun postGetCMRDataForValidation(@Header("Authorization") token:String, @Body json: JsonObject): Call<GetCMRDataForValidResp>

    @POST("api/cmr/getCMRtendata")
    fun postGetCMRTenData(@Header("Authorization") token:String, @Body json: JsonObject): Call<GetCMRTenDataResp>

    @POST("api/cmr/getCMRtwentlydata")
    fun postGetCMRTwentyData(@Header("Authorization") token:String, @Body json: JsonObject): Call<GetCMRTwentyDataResp>

    @POST("api/cmr/getCMRthirtydata")
    fun postGetCMRThirtyData(@Header("Authorization") token:String, @Body json: JsonObject): Call<GetCMRThirtyDataResp>

    @POST("api/cmr/getCMRfortydata")
    fun postGetCMRFortyData(@Header("Authorization") token:String, @Body json: JsonObject): Call<GetCMRFortyDataResp>

    @POST("api/cmr/moStaffwiserGrowerReport")
    fun postMoStaffWiserGrowerReport(@Header("Authorization") token:String, @Body json: JsonObject): Call<GetMoStaffWiserGrowerReport>

}