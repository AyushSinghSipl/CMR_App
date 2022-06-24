package com.mahyco.cmr_app.utils.firebasea_analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.mahyco.cmr_app.core.DLog

class FirebaseAnalyticsHelper {

    companion object {

        private var mFirebaseAnalytics: FirebaseAnalytics? = null
        private var helperInstance: FirebaseAnalyticsHelper? = null

        fun getInstance(context: Context?): FirebaseAnalyticsHelper? {
            // Obtain the FirebaseAnalytics instance.
            if (FirebaseAnalyticsHelper.helperInstance == null) {
                FirebaseAnalyticsHelper.helperInstance = FirebaseAnalyticsHelper()
            }
            if (FirebaseAnalyticsHelper.mFirebaseAnalytics == null) {
                FirebaseAnalyticsHelper.mFirebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
            }
            //return mFirebaseAnalytics;
            return FirebaseAnalyticsHelper.helperInstance
        }
    }

    private val CE_LOGIN = "ce_login_user"
    private val CE_LOGOUT = "ce_logout_user"
    private val CE_REGISTER = "ce_register_user"
    private val CE_APP_OPENED = "ce_app_opened"
    private val CE_FORGOT_PASSWORD = "ce_forgot_password"

    fun callLoginEvent(userId: String?, displayName: String?) { //1
        val bundle = Bundle()
        DLog.d("Call custom event login firebase")
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userId)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, displayName)
        bundle.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD, "ce_login_user")
        FirebaseAnalyticsHelper.mFirebaseAnalytics?.setUserId(userId)
        FirebaseAnalyticsHelper.mFirebaseAnalytics?.logEvent(CE_LOGIN, bundle)
    }

}