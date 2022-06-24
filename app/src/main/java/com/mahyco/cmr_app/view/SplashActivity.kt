package com.mahyco.cmr_app.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.mahyco.cmr_app.BuildConfig
import com.mahyco.cmr_app.MainActivity
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.core.BaseActivity
import com.mahyco.cmr_app.core.Constant
import com.mahyco.rcbucounterboys2020.utils.SharedPreference
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : BaseActivity() {

    private val SPLASH_TIME_OUT: Long = 3000 // 1 sec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val sharedPreference: SharedPreference = SharedPreference(this)
        //DLog.d("Val = "+10/0) \
        // throw RuntimeException("Test Crash") // Force a crash
        supportActionBar?.hide();
        val versionName = (this)
        tv_version.text = ""+versionName
        Handler().postDelayed({
            if (sharedPreference.getValueBoolean(Constant.IS_USER_LOGGED_IN, false) != null) {
                val isLoggedIn =
                    sharedPreference.getValueBoolean(Constant.IS_USER_LOGGED_IN, false)!!
                printLog("LOGGED Splash Is Logged in:$isLoggedIn")
                if (isLoggedIn) {
                    //showLongMessage("Already logged in")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    //showLongMessage("Login")
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
            finish()
        }, SPLASH_TIME_OUT)
    }


}