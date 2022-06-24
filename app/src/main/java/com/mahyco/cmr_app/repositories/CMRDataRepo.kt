package com.mahyco.cmr_app.repositories

import com.google.gson.JsonObject
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
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CMRDataRepo {

    /*30th March 2021*/
    fun postGetCMRDataForValAPI(token:String,jsonObject: JsonObject): Observable<GetCMRDataForValidResp>

    fun getVehicleList(): Observable<List<GetVehicleTypeResponseItem>>
    fun getActivityList(): Observable<List<GetActivityTypeResponseItem>>
    fun callLoginAPi(loginParam: LoginParam): Observable<LoginResponseModel>

    fun postGetCMRTenData(token:String,jsonObject: JsonObject): Observable<GetCMRTenDataResp>
    fun postTourEventData(list: List<TourEventParamItem>): Observable<GetTourEventResponse>
    fun realtimetourdata(list: List<TourEventParamItem>): Observable<GetTourEventResponse>

    fun postGetCMRTwentyData(token:String,jsonObject: JsonObject): Observable<GetCMRTwentyDataResp>

    fun postGetCMRThirtyData(token:String,jsonObject: JsonObject): Observable<GetCMRThirtyDataResp>

    fun postGetCMRFortyData(token:String,jsonObject: JsonObject): Observable<GetCMRFortyDataResp>

    fun postMoStaffWiserGrowerReport(token:String,jsonObject: JsonObject): Observable<GetMoStaffWiserGrowerReport>


}