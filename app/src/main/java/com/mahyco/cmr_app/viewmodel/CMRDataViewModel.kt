package com.mahyco.cmr_app.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.android.roomwordssample.Word
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.api.IDataServiceCMR
import com.mahyco.cmr_app.core.Constant
import com.mahyco.cmr_app.model.getActivityType.GetActivityTypeResponseItem
import com.mahyco.cmr_app.model.getCMRDataForValidation.GetCMRDataForValidResp
import com.mahyco.cmr_app.model.getCMRFortyDataResp.GetCMRFortyDataResp
import com.mahyco.cmr_app.model.getCMRTenDataResp.GetCMRTenDataResp
import com.mahyco.cmr_app.model.getCMRThirtyDataResp.GetCMRThirtyDataResp
import com.mahyco.cmr_app.model.getCMRTwentyDataResp.GetCMRTwentyDataResp
import com.mahyco.cmr_app.model.getEventTourData.GetTourEventResponse
import com.mahyco.cmr_app.model.getEventTourData.TourEventParamItem
import com.mahyco.cmr_app.model.getMoStaffwiserGrowerReport.GetMoStaffWiserGrowerReport
import com.mahyco.cmr_app.model.getVehicleTypeResponse.GetVehicleTypeResponseItem
import com.mahyco.cmr_app.repositories.ImplCMRDataRepo
import com.mahyco.isp.repositories.RetrofitApiClient
import com.mahyco.isp.viewmodel.BaseViewModel
import com.mahyco.rcbucounterboys2020.utils.EncryptDecryptManager
import com.mahyco.rcbucounterboys2020.utils.SharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okio.internal.commonToUtf8String
import java.io.IOException
import java.util.*


class CMRDataViewModel(application: Application) : BaseViewModel(application) {

    private var mContext: Context = application
    private var iServiceISP: IDataServiceCMR
    var cmrDataMutableLiveData = MutableLiveData<GetCMRDataForValidResp>()
    var getVehicleTypeData = MutableLiveData<List<GetVehicleTypeResponseItem>>()
    var getActivityTypeData = MutableLiveData<List<GetActivityTypeResponseItem>>()
    var postTourEventsdata = MutableLiveData<GetTourEventResponse>()
    var cmrDataMLDGetCMRTenData = MutableLiveData<GetCMRTenDataResp>()
    var cmrDataMLDGetCMRTwentyData = MutableLiveData<GetCMRTwentyDataResp>()
    var cmrDataMLDGetCMRThirtyData = MutableLiveData<GetCMRThirtyDataResp>()
    var cmrDataMLDGetCMRFortyData = MutableLiveData<GetCMRFortyDataResp>()
    var cmrMLMoStaffWiserGrowerReport = MutableLiveData<GetMoStaffWiserGrowerReport>()

    init {
        var apiClient = RetrofitApiClient.getAPIClient()
        iServiceISP = apiClient.create(IDataServiceCMR::class.java)
    }

    fun getVehicleType(

    ) {

        val sharedPreference: SharedPreference = SharedPreference(mContext)
        loadingLiveData.value = true
        val disposable = CompositeDisposable()
        val observable = ImplCMRDataRepo(iServiceISP).getVehicleList(
        )
        val dispose = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                loadingLiveData.value = false

                if (response != null /*&& response.message!=null*/) {
//                    if (response.getVehicleTypeResponse != null) {
                    getVehicleTypeData.value = response
                    val gson = Gson()
                    val json = gson.toJson(response)
                    sharedPreference.save(Constant.VEHICLE_LIST, json)
//                    } else {
////                        errorLiveData.value = response.message
//                    }

                } else {
                    errorLiveData.value = mContext.resources.getString(R.string.server_error)
                }
            }

        disposable.add(dispose)
    }

    fun getActivityType(

    ) {

        val sharedPreference: SharedPreference = SharedPreference(mContext)
        loadingLiveData.value = true
        val disposable = CompositeDisposable()
        val observable = ImplCMRDataRepo(iServiceISP).getActivityList(
        )
        val dispose = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                loadingLiveData.value = false

                if (response != null /*&& response.message!=null*/) {
//                    if (response.getVehicleTypeResponse != null) {
                    getActivityTypeData.value = response
                    val gson = Gson()
                    val json = gson.toJson(response)
                    sharedPreference.save(Constant.ACTIVITY_LIST, json)
//                    } else {
////                        errorLiveData.value = response.message
//                    }

                } else {
                    errorLiveData.value = mContext.resources.getString(R.string.server_error)
                }
            }

        disposable.add(dispose)
    }


    fun getCMRDataForValAPI(
        cntr_code: String,
        prd_year: String,
        staff_code: String,
        postatus: String,
        filename: String,
        userCode: String
    ) {

        val sharedPreference: SharedPreference = SharedPreference(mContext)
        val encryptedUserCode = sharedPreference.getValueString(Constant.USER_CODE)
        val decryptedUserCode = "" + EncryptDecryptManager.decryptStringData(encryptedUserCode)
        val accessToken = sharedPreference.getValueString(Constant.USER_TOKEN)
        val finalMessage = sharedPreference.getValueString(Constant.FINAL_MESSAGE)

        val jsonboj = JsonObject()
        val jsonParam = JsonObject()

        jsonParam.addProperty("access_token", accessToken)
        jsonParam.addProperty("finalmessage", finalMessage)
        //jsonParam.put("userCode", decryptedUserCode)
        //jsonParam.addProperty("userCode", "97180401")
        jsonParam.addProperty("userCode", userCode)
        jsonParam.addProperty("cntr_code", cntr_code)
        jsonParam.addProperty("prd_year", prd_year)
        jsonParam.addProperty("staff_code", staff_code)
        jsonParam.addProperty("postatus", postatus)
        jsonParam.addProperty("filename", filename)

        jsonboj.add("Table", jsonParam)

//        DLog.d("getCMRData JSON OBJ : $jsonboj")
        loadingLiveData.value = true
        val disposable = CompositeDisposable()
        val observable = ImplCMRDataRepo(iServiceISP).postGetCMRDataForValAPI(
            "Bearer $accessToken",
            jsonboj
        )
        val dispose = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                loadingLiveData.value = false

                if (response != null && response.message != null) {
                    if (response.success!!) {
                        cmrDataMutableLiveData.value = response
                    } else {
                        errorLiveData.value = response.message
                    }

                } else {
                    errorLiveData.value = mContext.resources.getString(R.string.server_error)
                }
            }

        disposable.add(dispose)
    }

    @SuppressLint("NewApi")
    fun postTourEventDataAPI(
        eventList: MutableList<Word>
    ) {

        val sharedPreference: SharedPreference = SharedPreference(mContext)
        val encryptedUserCode = sharedPreference.getValueString(Constant.USER_CODE)
        val decryptedUserCode = "" + EncryptDecryptManager.decryptStringData(encryptedUserCode)
        val accessToken = sharedPreference.getValueString(Constant.USER_TOKEN)
        val finalMessage = sharedPreference.getValueString(Constant.FINAL_MESSAGE)
        val emp_id = sharedPreference.getValueString(Constant.EMP_ID)

        var listTourEvent: MutableList<TourEventParamItem> = ArrayList()

        for (item in eventList) {
            if (item.uStatus == "0") {


                /*val startImage = Base64.encodeToString(item.uKmImageStart, Base64.DEFAULT)
                val endImage = Base64.encodeToString(item.uKmImageEnd, Base64.DEFAULT)
                val eventImage = Base64.encodeToString(item.uKmImageEvent, Base64.DEFAULT)*/
                var eventImage = ""
                var endImage = ""
                var startImage = ""
                try {


                   /* val decodedStringEvent: ByteArray =
                        android.util.Base64.decode(item.uKmImageEvent, android.util.Base64.DEFAULT)*/

                    eventImage = Base64.getEncoder().encodeToString(item.uKmImageEvent)

                   /* val decodedStringEnd: ByteArray =
                        android.util.Base64.decode(item.uKmImageEnd, android.util.Base64.DEFAULT)*/

                    endImage = Base64.getEncoder().encodeToString(item.uKmImageEnd)

                 /*   val decodedStringstart: ByteArray =
                        android.util.Base64.decode(item.uKmImageStart, android.util.Base64.DEFAULT)*/

                    startImage = Base64.getEncoder().encodeToString(item.uKmImageStart)
                } catch (e: IOException) {
                    Log.e("CRMDataViewModel", "postTourEventDataAPI:exception  " + e.message)
                }


                val startlatlng = item.uStartLat + "," + item.uStartLng
                val endlatlng = item.uEndLat + "," + item.uEndLng
                val eventlatlng = item.uEventLat + "," + item.uEventLng
                val tourEventParamItem = TourEventParamItem(
                    item.uStartDateTime,
                    eventImage,
                    item.uTourId.toInt(),
                    "1",
                    item.uVehicleId.toInt(),
                    item.uKmReadingStart.toInt(),
                    emp_id,
                    item.uId,
                    item.uEventDateTime,
                    endImage,
                    startlatlng,
                    item.uDate,
                    startImage.toString(),
                    Math.round(item.uKmReadingEnd.toFloat()),
                    eventlatlng,
                    item.uEndDateTime,
                    item.uEventId.toInt(),
                    item.uEventDescription,
                    item.uType,
                    endlatlng, "0", "0"
                )
                listTourEvent.add(tourEventParamItem)
            }


        }
        val gson = Gson()
        val json = gson.toJson(listTourEvent)

        loadingLiveData.value = true
        if (listTourEvent.size != 0) {
            val disposable = CompositeDisposable()
            val observable = ImplCMRDataRepo(iServiceISP).postTourEventData(
                listTourEvent
            )
            val dispose = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response ->
                    loadingLiveData.value = false

                    if (response != null) {
//                        postTourEventsdata.value = response
                        if (response.status.equals("Success", true)) {
                            postTourEventsdata.value = response

                        } else {
                            errorLiveData.value = response.errorMessage.toString()
                        }
                    } else {
                        errorLiveData.value = mContext.resources.getString(R.string.server_error)
                    }
                }

            disposable.add(dispose)
        } else {
            loadingLiveData.value = false
        }
    }

    fun realtimetourdata(
        eventList: MutableList<Word>
    ) {

        val sharedPreference: SharedPreference = SharedPreference(mContext)
        val encryptedUserCode = sharedPreference.getValueString(Constant.USER_CODE)
        val decryptedUserCode = "" + EncryptDecryptManager.decryptStringData(encryptedUserCode)
        val accessToken = sharedPreference.getValueString(Constant.USER_TOKEN)
        val finalMessage = sharedPreference.getValueString(Constant.FINAL_MESSAGE)
        val emp_id = sharedPreference.getValueString(Constant.EMP_ID)

        var listTourEvent: MutableList<TourEventParamItem> = ArrayList()

        for (item in eventList) {
            if (item.uStatus == "0") {
                val startlatlng = item.uStartLat + "," + item.uStartLng
                val endlatlng = item.uEndLat + "," + item.uEndLng
                val eventlatlng = item.uEventLat + "," + item.uEventLng
                val tourEventParamItem = TourEventParamItem(
                    item.uStartDateTime,
                    item.uKmImageEvent.toString(),
                    item.uTourId.toInt(),
                    "1",
                    item.uVehicleId.toInt(),
                    item.uKmReadingStart.toInt(),
                    emp_id,
                    item.uId,
                    item.uEventDateTime,
                    item.uKmImageEnd.commonToUtf8String(),
                    startlatlng,
                    item.uDate,
                    item.uKmImageStart.toString(),
                    Math.round(item.uKmReadingEnd.toFloat()),
                    eventlatlng,
                    item.uEndDateTime,
                    item.uEventId.toInt(),
                    item.uEventDescription,
                    item.uType,
                    endlatlng, "0", "0"
                )
                listTourEvent.add(tourEventParamItem)
            }
        }
        val gson = Gson()
        val json = gson.toJson(listTourEvent)

        loadingLiveData.value = true
        if (listTourEvent.size != 0) {
            val disposable = CompositeDisposable()
            val observable = ImplCMRDataRepo(iServiceISP).realtimetourdata(
                listTourEvent
            )
            val dispose = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response ->
                    loadingLiveData.value = false

                    if (response != null) {
                        postTourEventsdata.value = response
                        /*  if (response.success!!) {
                        cmrDataMutableLiveData.value = response
                    } else {
                        errorLiveData.value = response.message
                    }
*/
                    } else {
                        errorLiveData.value = mContext.resources.getString(R.string.server_error)
                    }
                }

            disposable.add(dispose)
        } else {
            loadingLiveData.value = false
        }
    }

    fun getCMRTenDataAPI(
        cntr_code: String,
        prd_year: String,
        staff_code: String,
        postatus: String,
        filename: String,
        userCode: String
    ) {

        val sharedPreference: SharedPreference = SharedPreference(mContext)
        val encryptedUserCode = sharedPreference.getValueString(Constant.USER_CODE)
        val decryptedUserCode = "" + EncryptDecryptManager.decryptStringData(encryptedUserCode)
        val accessToken = sharedPreference.getValueString(Constant.USER_TOKEN)
        val finalMessage = sharedPreference.getValueString(Constant.FINAL_MESSAGE)

        val jsonboj = JsonObject()
        val jsonParam = JsonObject()

        jsonParam.addProperty("access_token", accessToken)
        jsonParam.addProperty("finalmessage", finalMessage)
        //jsonParam.put("userCode", decryptedUserCode)
        //jsonParam.addProperty("userCode", "97180401")
        jsonParam.addProperty("userCode", userCode)
        jsonParam.addProperty("cntr_code", cntr_code)
        jsonParam.addProperty("prd_year", prd_year)
        jsonParam.addProperty("staff_code", staff_code)
        jsonParam.addProperty("postatus", postatus)
        jsonParam.addProperty("filename", filename)

        jsonboj.add("Table", jsonParam)

//        D.d("getCMRData JSON OBJ : $jsonboj")
        loadingLiveData.value = true
        val disposable = CompositeDisposable()
        val observable = ImplCMRDataRepo(iServiceISP).postGetCMRTenData(
            "Bearer $accessToken",
            jsonboj
        )
        val dispose = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                loadingLiveData.value = false

                if (response != null && response.message != null) {
                    if (response.success!!) {
                        cmrDataMLDGetCMRTenData.value = response
                    } else {
                        errorLiveData.value = response.message
                    }

                } else {
                    errorLiveData.value = mContext.resources.getString(R.string.server_error)
                }
            }

        disposable.add(dispose)
    }

    fun getCMRTwentyDataAPI(
        cntr_code: String,
        prd_year: String,
        staff_code: String,
        postatus: String,
        filename: String,
        userCode: String
    ) {

        val sharedPreference: SharedPreference = SharedPreference(mContext)
        val encryptedUserCode = sharedPreference.getValueString(Constant.USER_CODE)
        val decryptedUserCode = "" + EncryptDecryptManager.decryptStringData(encryptedUserCode)
        val accessToken = sharedPreference.getValueString(Constant.USER_TOKEN)
        val finalMessage = sharedPreference.getValueString(Constant.FINAL_MESSAGE)

        val jsonboj = JsonObject()
        val jsonParam = JsonObject()

        jsonParam.addProperty("access_token", accessToken)
        jsonParam.addProperty("finalmessage", finalMessage)
        //jsonParam.put("userCode", decryptedUserCode)
        //jsonParam.addProperty("userCode", "97180401")
        jsonParam.addProperty("userCode", userCode)
        jsonParam.addProperty("cntr_code", cntr_code)
        jsonParam.addProperty("prd_year", prd_year)
        jsonParam.addProperty("staff_code", staff_code)
        jsonParam.addProperty("postatus", postatus)
        jsonParam.addProperty("filename", filename)

        jsonboj.add("Table", jsonParam)

//        DLog.d("getCMRData JSON OBJ : $jsonboj")
        loadingLiveData.value = true
        val disposable = CompositeDisposable()
        val observable = ImplCMRDataRepo(iServiceISP).postGetCMRTwentyData(
            "Bearer $accessToken",
            jsonboj
        )
        val dispose = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                loadingLiveData.value = false

                if (response != null && response.message != null) {
                    if (response.success!!) {
                        cmrDataMLDGetCMRTwentyData.value = response
                    } else {
                        errorLiveData.value = response.message
                    }

                } else {
                    errorLiveData.value = mContext.resources.getString(R.string.server_error)
                }
            }

        disposable.add(dispose)
    }

    fun getCMRThirtyDataAPI(
        cntr_code: String,
        prd_year: String,
        staff_code: String,
        postatus: String,
        filename: String,
        userCode: String
    ) {

        val sharedPreference: SharedPreference = SharedPreference(mContext)
        val encryptedUserCode = sharedPreference.getValueString(Constant.USER_CODE)
        val decryptedUserCode = "" + EncryptDecryptManager.decryptStringData(encryptedUserCode)
        val accessToken = sharedPreference.getValueString(Constant.USER_TOKEN)
        val finalMessage = sharedPreference.getValueString(Constant.FINAL_MESSAGE)

        val jsonboj = JsonObject()
        val jsonParam = JsonObject()

        jsonParam.addProperty("access_token", accessToken)
        jsonParam.addProperty("finalmessage", finalMessage)
        //jsonParam.put("userCode", decryptedUserCode)
        //jsonParam.addProperty("userCode", "97180401")
        jsonParam.addProperty("userCode", userCode)
        jsonParam.addProperty("cntr_code", cntr_code)
        jsonParam.addProperty("prd_year", prd_year)
        jsonParam.addProperty("staff_code", staff_code)
        jsonParam.addProperty("postatus", postatus)
        jsonParam.addProperty("filename", filename)

        jsonboj.add("Table", jsonParam)

//        DLog.d("getCMRData JSON OBJ : $jsonboj")
        loadingLiveData.value = true
        val disposable = CompositeDisposable()
        val observable = ImplCMRDataRepo(iServiceISP).postGetCMRThirtyData(
            "Bearer $accessToken",
            jsonboj
        )
        val dispose = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                loadingLiveData.value = false

                if (response != null && response.message != null) {
                    if (response.success!!) {
                        cmrDataMLDGetCMRThirtyData.value = response
                    } else {
                        errorLiveData.value = response.message
                    }

                } else {
                    errorLiveData.value = mContext.resources.getString(R.string.server_error)
                }
            }

        disposable.add(dispose)
    }

    fun getCMRFortyDataAPI(
        cntr_code: String,
        prd_year: String,
        staff_code: String,
        postatus: String,
        filename: String,
        userCode: String
    ) {

        val sharedPreference: SharedPreference = SharedPreference(mContext)
        val encryptedUserCode = sharedPreference.getValueString(Constant.USER_CODE)
        val decryptedUserCode = "" + EncryptDecryptManager.decryptStringData(encryptedUserCode)
        val accessToken = sharedPreference.getValueString(Constant.USER_TOKEN)
        val finalMessage = sharedPreference.getValueString(Constant.FINAL_MESSAGE)

        val jsonboj = JsonObject()
        val jsonParam = JsonObject()

        jsonParam.addProperty("access_token", accessToken)
        jsonParam.addProperty("finalmessage", finalMessage)
        //jsonParam.put("userCode", decryptedUserCode)
        //jsonParam.addProperty("userCode", "97180401")
        jsonParam.addProperty("userCode", userCode)
        jsonParam.addProperty("cntr_code", cntr_code)
        jsonParam.addProperty("prd_year", prd_year)
        jsonParam.addProperty("staff_code", staff_code)
        jsonParam.addProperty("postatus", postatus)
        jsonParam.addProperty("filename", filename)

        jsonboj.add("Table", jsonParam)

//        DLog.d("getCMRData JSON OBJ : $jsonboj")
        loadingLiveData.value = true
        val disposable = CompositeDisposable()
        val observable = ImplCMRDataRepo(iServiceISP).postGetCMRFortyData(
            "Bearer $accessToken",
            jsonboj
        )
        val dispose = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                loadingLiveData.value = false

                if (response != null && response.message != null) {
                    if (response.success!!) {
                        cmrDataMLDGetCMRFortyData.value = response
                    } else {
                        errorLiveData.value = response.message
                    }

                } else {
                    errorLiveData.value = mContext.resources.getString(R.string.server_error)
                }
            }

        disposable.add(dispose)
    }


    fun getMO(
        cntr_code: String,
        prd_year: String,
        staff_code: String,
        postatus: String,
        filename: String,
        userCode: String
    ) {

        val sharedPreference: SharedPreference = SharedPreference(mContext)
        val encryptedUserCode = sharedPreference.getValueString(Constant.USER_CODE)
        val decryptedUserCode = "" + EncryptDecryptManager.decryptStringData(encryptedUserCode)
        val accessToken = sharedPreference.getValueString(Constant.USER_TOKEN)
        val finalMessage = sharedPreference.getValueString(Constant.FINAL_MESSAGE)

        val jsonboj = JsonObject()
        val jsonParam = JsonObject()

        jsonParam.addProperty("access_token", accessToken)
        jsonParam.addProperty("finalmessage", finalMessage)
        //jsonParam.put("userCode", decryptedUserCode)
        //jsonParam.addProperty("userCode", "97180401")
        jsonParam.addProperty("userCode", userCode)
        jsonParam.addProperty("cntr_code", cntr_code)
        jsonParam.addProperty("prd_year", prd_year)
        jsonParam.addProperty("staff_code", staff_code)
        jsonParam.addProperty("postatus", postatus)
        jsonParam.addProperty("filename", filename)

        jsonboj.add("Table", jsonParam)

//        DLog.d("getCMRData JSON OBJ : $jsonboj")
        loadingLiveData.value = true
        val disposable = CompositeDisposable()
        val observable = ImplCMRDataRepo(iServiceISP).postGetCMRFortyData(
            "Bearer $accessToken",
            jsonboj
        )
        val dispose = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                loadingLiveData.value = false

                if (response != null && response.message != null) {
                    if (response.success!!) {
                        cmrDataMLDGetCMRFortyData.value = response
                    } else {
                        errorLiveData.value = response.message
                    }

                } else {
                    errorLiveData.value = mContext.resources.getString(R.string.server_error)
                }
            }

        disposable.add(dispose)
    }


    fun getMOStaffWiserGrowerReport(
        cntr_code: String,
        prd_year: String,
        staff_code: String,
        postatus: String,
        filename: String,
        userCode: String,
        prdCode: String,
        TFACode: String
    ) {

        val sharedPreference: SharedPreference = SharedPreference(mContext)
        val encryptedUserCode = sharedPreference.getValueString(Constant.USER_CODE)
        val decryptedUserCode = "" + EncryptDecryptManager.decryptStringData(encryptedUserCode)
        val accessToken = sharedPreference.getValueString(Constant.USER_TOKEN)
        val finalMessage = sharedPreference.getValueString(Constant.FINAL_MESSAGE)

        val jsonboj = JsonObject()
        val jsonParam = JsonObject()

        jsonParam.addProperty("access_token", accessToken)
        jsonParam.addProperty("finalmessage", finalMessage)
        //jsonParam.put("userCode", decryptedUserCode)
        //jsonParam.addProperty("userCode", "97180401")
        jsonParam.addProperty("userCode", userCode)
        jsonParam.addProperty("cntr_code", cntr_code)
        jsonParam.addProperty("prd_year", prd_year)
        jsonParam.addProperty("prd_code", prdCode)
        jsonParam.addProperty("staff_code", staff_code)
        jsonParam.addProperty("TFA_code", TFACode)
        jsonParam.addProperty("postatus", postatus)
        jsonParam.addProperty("filename", filename)

        jsonboj.add("Table", jsonParam)

//        DLog.d("getCMRData JSON OBJ : $jsonboj")
        loadingLiveData.value = true
        val disposable = CompositeDisposable()
        val observable = ImplCMRDataRepo(iServiceISP).postMoStaffWiserGrowerReport(
            "Bearer $accessToken",
            jsonboj
        )
        val dispose = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                loadingLiveData.value = false

                if (response != null && response.message != null) {
                    if (response.success!!) {
                        cmrMLMoStaffWiserGrowerReport.value = response
                    } else {
                        errorLiveData.value = response.message
                    }

                } else {
                    errorLiveData.value = mContext.resources.getString(R.string.server_error)
                }
            }

        disposable.add(dispose)
    }
}