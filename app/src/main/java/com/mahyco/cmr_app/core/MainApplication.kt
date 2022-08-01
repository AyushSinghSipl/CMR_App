package com.mahyco.isp.core

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.example.android.roomwordssample.WordRepository
import com.example.android.roomwordssample.WordRoomDatabase
import com.mahyco.cmr_app.core.Constant
import com.mahyco.cmr_app.core.DLog
import com.mahyco.cmr_app.leegality.Response.LeegalityMainResponse
import com.mahyco.cmr_app.model.vendordetailsforcontract.VendorRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplication : Application() {

    lateinit var vendorRecord: VendorRecord
    val applicationScope = CoroutineScope(SupervisorJob())
    lateinit var leegalityMainResponse: LeegalityMainResponse



    val database by lazy { WordRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { WordRepository(database.wordDao()) }



    init {
        instance = this
    }

    companion object {
        var ctx: Context? = null
        var instance: MainApplication? = null

        fun applicationContext() : MainApplication {
            return instance as MainApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        ctx = applicationContext
        // set value
        Constant.IS_DEBUGGABLE = CommonMethods.isDebuggable(applicationContext)

        Log.d("LOGS","IS_Debuggable : "+Constant.IS_DEBUGGABLE);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


    }

    fun setVDResponse(vRecord: VendorRecord){
       vendorRecord = vRecord
       DLog.d("SET Vendor Records : =====================\n$vendorRecord")
    }

    fun getVDRecords():VendorRecord{
        DLog.d("GET Vendor Records : =====================\n$vendorRecord")
        return  vendorRecord
    }

    fun setMainResponse(mainResponse: LeegalityMainResponse){
        leegalityMainResponse = mainResponse
        DLog.d("SET LeegalityMainResponse : =====================\n$mainResponse")
    }

    fun getLMainResponse():LeegalityMainResponse{
        DLog.d("GET LeegalityMainResponse : =====================\n$leegalityMainResponse")
        return  leegalityMainResponse
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this);
    }
}