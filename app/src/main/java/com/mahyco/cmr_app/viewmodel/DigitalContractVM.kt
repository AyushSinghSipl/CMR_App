package com.mahyco.cmr_app.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.api.IDataServiceCMR
import com.mahyco.cmr_app.core.DLog
import com.mahyco.cmr_app.model.getProductListResponse.GetProductList
import com.mahyco.cmr_app.model.getSeasonListResponse.GetSeasonsResponse
import com.mahyco.cmr_app.model.getVendorListResponse.VendorListResponse
import com.mahyco.cmr_app.model.getcenterlistresponse.GetCenterListResponse
import com.mahyco.cmr_app.model.vendordetailsforcontract.VDContractResponse
import com.mahyco.cmr_app.repositories.ImplDigitalContractRepo
import com.mahyco.isp.repositories.RetrofitApiClient
import com.mahyco.isp.viewmodel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DigitalContractVM(application: Application) : BaseViewModel(application){
    private var mContext: Context = application
    private var iServiceCMR: IDataServiceCMR

    var seasonListDataMLData = MutableLiveData<GetSeasonsResponse>()
    var vendorListDataMLData = MutableLiveData<VendorListResponse>()
    var productListDataMLData = MutableLiveData<GetProductList>()
    var centerListMLData = MutableLiveData<GetCenterListResponse>()
    var vendorDetailsMLData = MutableLiveData<VDContractResponse>()

    init {
        var apiClient = RetrofitApiClient.getAPIClient()
        iServiceCMR = apiClient.create(IDataServiceCMR::class.java)
    }

    fun getSeasonListVM(){
        loadingLiveData.value = true
        val disposable = CompositeDisposable()

        val observable = ImplDigitalContractRepo(iServiceCMR).getGetSeasonList()
        val dispose = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                loadingLiveData.value = false
                if (response != null && response.success !=null) {
                    DLog.d("Response getSeasonList success:" + response.success + " message:" + response.msg)
                    if (response.success!!) {
                        seasonListDataMLData.value = response
                    } else {
                        errorLiveData.value = response.msg
                    }
                } else {
                    errorLiveData.value = mContext.resources.getString(R.string.server_error)
                }
            }

        disposable.add(dispose)
    }

    fun getVendorList(centerCode: String, prdSeason: String, prdYear: String, userCode: String){

        loadingLiveData.value = true
        val disposable = CompositeDisposable()

        val jsonboj = JsonObject()
        val jsonParam = JsonObject()
        jsonParam.addProperty("cntr_code", centerCode)
        jsonParam.addProperty("Prd_season", prdSeason)
        jsonParam.addProperty("prd_year", prdYear)
        jsonParam.addProperty("user_code",userCode)
        jsonboj.add("Table", jsonParam)

        val observable = ImplDigitalContractRepo(iServiceCMR).postGetVendorList("",jsonboj)
        val dispose = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                loadingLiveData.value = false
                if (response != null && response.success !=null) {
                    DLog.d("Response getSeasonList success:" + response.success + " message:" + response.msg)
                    if (response.success==true) {
                        vendorListDataMLData.value = response
                    } else {
                        errorLiveData.value = response.msg
                    }
                } else {
                    errorLiveData.value = mContext.resources.getString(R.string.server_error)
                }
            }

        disposable.add(dispose)
    }


    fun getProductList(centerCode: String, prdSeason: String, prdYear: String, userCode: String, vendorCode:String){

        loadingLiveData.value = true
        val disposable = CompositeDisposable()

        val jsonboj = JsonObject()
        val jsonParam = JsonObject()
        jsonParam.addProperty("cntr_code", centerCode)
        jsonParam.addProperty("Prd_season", prdSeason)
        jsonParam.addProperty("prd_year", prdYear)
        jsonParam.addProperty("user_code",userCode)
        jsonParam.addProperty("vendorcode",vendorCode)
        jsonboj.add("Table", jsonParam)

        val observable = ImplDigitalContractRepo(iServiceCMR).postGetProductList("",jsonboj)
        val dispose = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                loadingLiveData.value = false
                if (response != null && response.success !=null) {
                    DLog.d("Response getSeasonList success:" + response.success + " message:" + response.msg)
                    if (response.success!!) {
                        productListDataMLData.value = response
                    } else {
                        errorLiveData.value = response.msg
                    }
                } else {
                    errorLiveData.value = mContext.resources.getString(R.string.server_error)
                }
            }

        disposable.add(dispose)
    }

    fun getCenterList(userCode: String){

        loadingLiveData.value = true
        val disposable = CompositeDisposable()

        val observable = ImplDigitalContractRepo(iServiceCMR).getCenterList(userCode)
        val dispose = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                loadingLiveData.value = false
                if (response != null && response.success !=null) {
                    DLog.d("Response getSeasonList success:" + response.success + " message:" + response.msg)
                    if (response.success!!) {
                        centerListMLData.value = response
                    } else {
                        errorLiveData.value = response.msg
                    }
                } else {
                    errorLiveData.value = mContext.resources.getString(R.string.server_error)
                }
            }

        disposable.add(dispose)
    }

    fun getVendorDetailsForContract(userCode: String, centerCode: String,vendorCode: String,year: String,season:String,prdCode:String){
        loadingLiveData.value = true
        val disposable = CompositeDisposable()

        val jsonboj = JsonObject()
        val jsonParam = JsonObject()
        jsonParam.addProperty("Action", "1")
        jsonParam.addProperty("userCode", userCode)
        jsonParam.addProperty("centerCode", centerCode)
        jsonParam.addProperty("vendorcode", vendorCode)
        jsonParam.addProperty("year",year)
        jsonParam.addProperty("season",season)
        jsonParam.addProperty("prd_code",prdCode)
        jsonboj.add("Table", jsonParam)

        val observable = ImplDigitalContractRepo(iServiceCMR).postGetVendorDetailsForContract("",jsonboj)
        val dispose = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response ->
                    loadingLiveData.value = false
                    if (response != null && response.success !=null) {
                        DLog.d("Response getSeasonList success:" + response.success + " message:" + response.msg)
                        if (response.success!!) {
                            vendorDetailsMLData.value = response
                        } else {
                            errorLiveData.value = response.msg
                        }
                    } else {
                        errorLiveData.value = mContext.resources.getString(R.string.server_error)
                    }
                }

        disposable.add(dispose)
    }

}