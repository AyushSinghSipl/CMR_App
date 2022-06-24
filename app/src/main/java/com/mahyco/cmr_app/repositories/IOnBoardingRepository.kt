package com.mahyco.cmr_app.repositories

import com.mahyco.cmr_app.model.BaseResponse
import io.reactivex.Observable

interface IOnBoardingRepository {

    fun getToken (type: String, userName : String, sapcode : String , pwd: String): Observable<BaseResponse>

}