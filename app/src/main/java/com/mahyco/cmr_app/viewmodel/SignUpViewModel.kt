package com.mahyco.cmr_app.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.api.IDataServiceCMR
import com.mahyco.cmr_app.api.IOnBoardingService
import com.mahyco.cmr_app.core.Constant
import com.mahyco.cmr_app.core.DLog
import com.mahyco.cmr_app.model.login.LoginParam
import com.mahyco.cmr_app.model.login.LoginResponseModel
import com.mahyco.cmr_app.repositories.ImplCMRDataRepo
import com.mahyco.cmr_app.repositories.OnBoardingRepositoryImpl
import com.mahyco.isp.repositories.RetrofitApiClient
import com.mahyco.isp.viewmodel.BaseViewModel
import com.mahyco.rcbucounterboys2020.utils.EncryptDecryptManager
import com.mahyco.rcbucounterboys2020.utils.SharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SignUpViewModel(application: Application) : BaseViewModel(application) {


    private var mContext: Context = application
    private var iActivationAPIService: IOnBoardingService
    private var iServiceISP: IDataServiceCMR

    var userData = MutableLiveData<LoginResponseModel>()


    init {
        var apiClient = RetrofitApiClient.getAPIClient()
        iActivationAPIService = apiClient.create(IOnBoardingService::class.java)
        iServiceISP = apiClient.create(IDataServiceCMR::class.java)
    }

     fun SignUpViewModel() {}

    fun callLoginApi(
       loginParam: LoginParam
    ){

        val sharedPreference: SharedPreference = SharedPreference(mContext)
        loadingLiveData.value = true
        val disposable = CompositeDisposable()
        val observable = ImplCMRDataRepo(iServiceISP).callLoginAPi(loginParam
        )
        val dispose = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                loadingLiveData.value = false

                if (response != null /*&& response.message!=null*/) {
//                    if (response.getVehicleTypeResponse != null) {
                    userData.value = response

//                    } else {
////                        errorLiveData.value = response.message
//                    }

                } else {
                    errorLiveData.value = mContext.resources.getString(R.string.server_error)
                }
            }

        disposable.add(dispose)
    }

    fun getTokenForAPI(userName: String, sapcode:String, pwd: String) {
        loadingLiveData.value = true
        val disposable = CompositeDisposable()
        val observable = OnBoardingRepositoryImpl(iActivationAPIService).getToken(
                "password",
                userName,
                sapcode,
                pwd
        )
        val dispose = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    loadingLiveData.value = false
                    if (Constant.SUCCESS_STATUS.equals(it.errorCodeData, ignoreCase = true)) {
                        DLog.d("Response : "+it.getJsonObject().toString())
                        val sharedPreference: SharedPreference = SharedPreference(mContext)
/*                    val accessToken =  "" + response.jsonObjectData!!.get("access_token")
                    val tokenType =  "" + response.jsonObjectData!!.get("token_type")*/
                        val userToken =  it.jsonObjectData!!.get("access_token").toString()
                        val typeToken = it.jsonObjectData!!.get("token_type").toString()
                        val finalmessage1 = it.jsonObjectData!!.get("finalmessage").toString()
                        val userName1 = it.jsonObjectData!!.get("userName").toString()
                        var accessToken = userToken?.replace("\"", "")
                        var tokenType = typeToken?.replace("\"", "")
                        var finalMessage = finalmessage1?.replace("\"", "")
                        var userName = userName1?.replace("\"", "")

                        val userNameEncrypted = ""+ EncryptDecryptManager.encryptStringData(userName)
                        sharedPreference.save(Constant.USER_NAME,userNameEncrypted )

                        sharedPreference.save(Constant.USER_TOKEN,accessToken)
                        sharedPreference.save(Constant.TOKEN_TYPE,tokenType )
                        sharedPreference.save(Constant.FINAL_MESSAGE,finalMessage )
                        sharedPreference.save(Constant.IS_USER_LOGGED_IN,true)

                        val userToken1 = sharedPreference.getValueString(Constant.USER_TOKEN);
                        val bearerType = sharedPreference.getValueString(Constant.TOKEN_TYPE);
                        DLog.d("CMR User Token in:$userToken1")
                        DLog.d("CMR Bearer type:$bearerType")

                        successLiveData.value = true

                    } else {
                        errorLiveData.value = it.errorMessageData
                    }
                }

        disposable.add(dispose)

    }

}