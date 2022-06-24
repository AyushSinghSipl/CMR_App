package com.mahyco.cmr_app.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mahyco.cmr_app.MainActivity
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.core.BaseActivity
import com.mahyco.cmr_app.core.Constant
import com.mahyco.cmr_app.core.DLog
import com.mahyco.cmr_app.core.Messageclass
import com.mahyco.cmr_app.model.login.LoginParam
import com.mahyco.cmr_app.utils.firebasea_analytics.FirebaseAnalyticsHelper
import com.mahyco.cmr_app.viewmodel.SignUpViewModel
import com.mahyco.rcbucounterboys2020.utils.EncryptDecryptManager
import com.mahyco.rcbucounterboys2020.utils.SharedPreference
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : BaseActivity() {

    lateinit var context: Context
    var signUpViewModel: SignUpViewModel? = null
    var device_Unique_id = ""
    var msclass: Messageclass? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //testcrash()
        context = this
        //Register Observer only onCreate
        msclass = Messageclass(this)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getDeviceIMEI()
        }
        registerObserver()
        setUI()

        //  edt_emp_code.setText("97180401")


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDeviceIMEI() {
        val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                3000
            )
            return
        }
        var IMEINumber = ""
        try {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                IMEINumber = telephonyManager.imei
            }
            device_Unique_id = IMEINumber
        } catch (e: Exception) {
            val uniquePseudoID =
                "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10
            val serial = Build.getRadioVersion()
            val uuid: String =
                UUID(uniquePseudoID.hashCode().toLong(), serial.hashCode().toLong()).toString()
            val brand = Build.BRAND
            val modelno = Build.MODEL
            val version = Build.VERSION.RELEASE
            device_Unique_id = uuid
            Log.e(
                "IMEI", """fetchDeviceInfo: 
 
 uuid is : $uuid
 brand is: $brand
 model is: $modelno
 version is: $version"""
            )
        }
        Log.e("IMEI", "onCreate: " + IMEINumber)
        //  return IMEINumber
    }

    private fun fetchDeviceInfo() {
        val uniquePseudoID =
            "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10
        val serial = Build.getRadioVersion()
        val uuid: String =
            UUID(uniquePseudoID.hashCode().toLong(), serial.hashCode().toLong()).toString()
        val brand = Build.BRAND
        val modelno = Build.MODEL
        val version = Build.VERSION.RELEASE
        device_Unique_id = uuid
        Log.e(
            "IMEI", """fetchDeviceInfo: 
 uuid is : $uuid
 brand is: $brand
 model is: $modelno
 version is: $version"""
        )
    }

    fun saveUserCode(context: Context, token: String?) {
        val empCode = "" + token
        EncryptDecryptManager.saveUserCodeWithEncryption(empCode, context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setUI() {

        supportActionBar?.title = getString(R.string.app_name_long)

        tv_skip_login.setOnClickListener(View.OnClickListener {
            //  saveUserCode(context)
            /*  val intent = Intent(this, MainActivity::class.java)
              //intent.putExtra("key", value)
              startActivityThis(intent)*/
        })

        tv_forgot_pass.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivityThis(intent)
        })

        tv_register.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivityThis(intent)
        })

        btn_login.setOnClickListener(View.OnClickListener {
            // saveUserCode(context)
//            logLoginEvent();
            if (validateData()) {
                callLogin()
            }
        })
        try {
            val pInfo: PackageInfo =
                context.packageManager.getPackageInfo(context.packageName, 0)
            val version: String = pInfo.versionName
            textViewVersionName.text = "version : " + version
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        requestToken()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun validateData(): Boolean {
        if (edt_emp_code.text.toString() == "") {
            msclass?.showMessage("Please enter emp code")
            return false
        }
        if (edt_password.text.toString() == "") {
            msclass?.showMessage("Please enter password")
            return false
        }
        if (device_Unique_id == "") {
            msclass?.showMessage("empty device id")
            getDeviceIMEI()
            return false
        }
        return true
    }

    private fun callLogin() {

        val loginParam =
            LoginParam(edt_emp_code.text.toString(), device_Unique_id, edt_password.text.toString())
        signUpViewModel?.callLoginApi(loginParam)
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getDeviceIMEI()
        }
    }

    private fun requestToken() {
        if (isNetworkAvailable(this@LoginActivity)) {
            //val mobileNo = edt_phone_no.text.toString()
            DLog.d("CMR GET TOKEN")
            signUpViewModel?.getTokenForAPI("97180401", "97180401", "test@123")
        } else {
            showLongMessage(resources.getString(R.string.no_internet))
        }
    }


    fun testcrash() {
        val crashButton = Button(this)
        crashButton.text = "Crash!"
        crashButton.setOnClickListener {
            throw RuntimeException("Test Crash") // Force a crash
        }

        addContentView(
            crashButton, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
    }

    private fun logLoginEvent() {
        val sharedPreference: SharedPreference = SharedPreference(context)
        val userCode = EncryptDecryptManager.getDecryptedUserCode(this@LoginActivity)
        if (sharedPreference != null) {
            if (userCode != null) {
                FirebaseAnalyticsHelper.getInstance(this)?.callLoginEvent(userCode, "")
            }
        }
    }

    private fun registerObserver() {
        signUpViewModel =
            ViewModelProviders.of(this).get<SignUpViewModel>(SignUpViewModel::class.java)

        //For handling Progress bar
        signUpViewModel!!.loadingLiveData.observe(this, Observer {
            if (it) {
                llProgressBar.visibility = View.VISIBLE
            } else {
                llProgressBar.visibility = View.GONE
            }
        })

        //In Case of error will show error in  toast message
        signUpViewModel!!.errorLiveData.observe(this, Observer {
            if (it != null)
                showShortMessage(it)
        })


        signUpViewModel!!.userData.observe(this, Observer {
            var result = it

            if (result.response?.isValid == true) {
                saveUserCode(context, result.response?.token)
                val sharedPreference: SharedPreference = SharedPreference(this)
                sharedPreference.save(Constant.IS_USER_LOGGED_IN, true)
                val userNameEncrypted =
                    "" + EncryptDecryptManager.encryptStringData(result?.response?.name)
                sharedPreference.save(Constant.USER_NAME, userNameEncrypted)
                sharedPreference.save(Constant.EMP_ID, edt_emp_code.text.toString())
                val intent = Intent(this, MainActivity::class.java)
                startActivityThis(intent)
                finish()
            } else {
                msclass?.showMessage("Invalid username and password!")
            }
        })


    }
}