package com.mahyco.cmr_app.repositories

import com.google.gson.JsonObject
import com.mahyco.cmr_app.model.getCMRDataForValidation.GetCMRDataForValidResp
import com.mahyco.cmr_app.model.getProductListResponse.GetProductList
import com.mahyco.cmr_app.model.getSeasonListResponse.GetSeasonsResponse
import com.mahyco.cmr_app.model.getVendorListResponse.VendorListResponse
import com.mahyco.cmr_app.model.getcenterlistresponse.GetCenterListResponse
import com.mahyco.cmr_app.model.vendordetailsforcontract.VDContractResponse
import io.reactivex.Observable

interface DigitalContractRepo {

    fun getGetSeasonList(): Observable<GetSeasonsResponse>
    fun getCenterList (userCode : String): Observable<GetCenterListResponse>
    fun postGetVendorList(token:String,jsonObject: JsonObject): Observable<VendorListResponse>
    fun postGetProductList(token:String,jsonObject: JsonObject): Observable<GetProductList>
    fun postGetVendorDetailsForContract(token:String,jsonObject: JsonObject): Observable<VDContractResponse>
}