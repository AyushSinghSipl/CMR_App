package com.mahyco.cmr_app

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.android.roomwordssample.Word
import com.example.android.roomwordssample.WordViewModel
import com.example.android.roomwordssample.WordViewModelFactory
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mahyco.cmr_app.core.BaseActivity
import com.mahyco.cmr_app.core.Constant
import com.mahyco.cmr_app.core.Messageclass
import com.mahyco.cmr_app.model.getActivityType.GetActivityTypeResponseItem
import com.mahyco.cmr_app.model.getVehicleTypeResponse.GetVehicleTypeResponseItem
import com.mahyco.cmr_app.view.*
import com.mahyco.cmr_app.viewmodel.CMRDataViewModel
import com.mahyco.isp.core.MainApplication
import com.mahyco.rcbucounterboys2020.utils.SharedPreference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Type


class MainActivity : BaseActivity() {
    var cmrDataViewModel: CMRDataViewModel? = null
    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory(
            (application as MainApplication).repository,
            application as MainApplication
        )
    }
    private var tourList: MutableList<Word> = ArrayList()
    var msclass: Messageclass? = null
    var uploaded = true

    private val REQ_CODE_VERSION_UPDATE = 530
    private var appUpdateManager: AppUpdateManager? = null
    private var installStateUpdatedListener: InstallStateUpdatedListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = getString(R.string.app_name_long)
        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            supportActionBar?.setIcon(getDrawable(R.mipmap.ic_launcher))
        }*/
        setUI()
        registerObserver()
        msclass = Messageclass(this)



//        checkNewAppVersionState()

        try {
            val pInfo: PackageInfo =
                packageManager.getPackageInfo(packageName, 0)
            val version: String = pInfo.versionName
            textViewVersionName.text = "version : " + version
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }


    }




    private fun registerObserver() {
        cmrDataViewModel =
            ViewModelProviders.of(this)
                .get<CMRDataViewModel>(CMRDataViewModel::class.java)

        //        For handling Progress bar
        cmrDataViewModel!!.loadingLiveData.observe(this, Observer {
            if (it) {
                llProgressBar.visibility = View.VISIBLE
            } else {
                llProgressBar.visibility = View.GONE
            }
        })

//        In Case of error will show error in  toast message
        cmrDataViewModel!!.errorLiveData.observe(this, Observer {
            if (it != null) {
                showShortMessage(it)
//                DLog.d("CMR errorLiveData :" + it)
            }
        })


        cmrDataViewModel!!.getVehicleTypeData.observe(this, Observer {
            var result = it.toString()
//            DLog.d("CMR DATA Response : " + result)
        })

        cmrDataViewModel!!.getActivityTypeData.observe(this, Observer {
            var result = it.toString()
//            DLog.d("CMR DATA Activity : " + result)
        })
        cmrDataViewModel!!.postTourEventsdata.observe(this, Observer {
            var result = it.toString()
            uploaded = true
        })

    }

    override fun onResume() {
        super.onResume()
        uploaded = true
        //    checkNewAppVersionState()
    }


    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission()
            }
        }
    }

    fun setUI() {
        checkLocationPermission()

        if (!isLocationEnabled(this)) {
            // notify user

            AlertDialog.Builder(this)
                .setMessage("PLease enable your location from setting")
                .setPositiveButton("Enable", DialogInterface.OnClickListener { dialogInterface, i ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

                })
                .setNegativeButton("Cancel", null)
                .show()
        }

        downloadCrmData.setOnClickListener {
            if (Constant.isNetworkConnected(this)) {
                cmrDataViewModel?.getVehicleType()
                cmrDataViewModel?.getActivityType()
            } else {
                msclass?.showMessage("No internet connection found")
            }
        }

        cd_digital_contract.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DCVendorFormActivity::class.java)
            startActivityThis(intent)
        })

        cd_download_grower_list.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DownloadActivity::class.java)
            startActivityThis(intent)
        })

        cd_download_cmr_data.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DownloadCMRDataActivity::class.java)
            startActivityThis(intent)
        })

        cd_field_area_tag.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, FieldAreaTagActivity::class.java)
            startActivityThis(intent)
        })

        cd_cmr_entry.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, CMREntryActivity::class.java)
            startActivityThis(intent)
        })

        cd_cmr_update.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, CMRUpdateActivity::class.java)
            startActivityThis(intent)
        })

        cd_cmr_validation.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, CMRValidationActivity::class.java)
            startActivityThis(intent)
        })

        cd_cmr_grower_info.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, GrowerInfoActivity::class.java)
            startActivityThis(intent)
        })

        cd_be_voice_of_grower.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, BeVoiceOfGrowerActivity::class.java)
            startActivityThis(intent)
        })

        cd_voct.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, VOCTActivity::class.java)
            startActivityThis(intent)
        })

        /*cd_upload_sync.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DataUploadSyncActivity::class.java)
            startActivityThis(intent)
        })*/

        cd_upload_sync.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, UploadSyncActivity::class.java)
            startActivityThis(intent)
        })

        cd_cmr_my_travel_report.setOnClickListener(View.OnClickListener {
            /* val intent = Intent(this, UploadSyncActivity::class.java)
             startActivityThis(intent)*/

            Toast.makeText(this, "Under development", Toast.LENGTH_SHORT).show()
        })

        cd_cmr_my_travel.setOnClickListener(View.OnClickListener {
            if (!isLocationEnabled(this)) {
                // notify user

                AlertDialog.Builder(this)
                    .setMessage("PLease enable your location from setting")
                    .setPositiveButton(
                        "Enable",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            startActivityForResult(
                                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                6000
                            )

                        })
                    .setNegativeButton("Cancel", null)
                    .show()
            }
            else {
                val sharedPreference: SharedPreference = SharedPreference(this)
                val gson = Gson()
                val jsonVehicle: String =
                    sharedPreference.getValueString(Constant.VEHICLE_LIST).toString()
                val typeVehicle: Type =
                    object : TypeToken<List<GetVehicleTypeResponseItem?>?>() {}.type
                val jsonActivity: String =
                    sharedPreference.getValueString(Constant.ACTIVITY_LIST).toString()
                val typeActivity: Type =
                    object : TypeToken<List<GetActivityTypeResponseItem?>?>() {}.type

                if (jsonVehicle != "null" && jsonActivity != "null") {
                    val Vehicle: List<GetVehicleTypeResponseItem> =
                        gson.fromJson(jsonVehicle, typeVehicle)
                    val activityList: List<GetActivityTypeResponseItem> =
                        gson.fromJson(jsonActivity, typeActivity)
                    if (Vehicle != null && Vehicle.size != 0 && activityList != null && activityList.size != 0) {
                        val intent = Intent(this, MyTravelActivity::class.java)
                        startActivityThis(intent)
                    } else {
                        downloadData()
                    }
                } else {
                    downloadData()
                }
            }

        })

        cd_cmr_grower_issues.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, GrowerIssueActivity::class.java)
            startActivityThis(intent)
        })
    }

    private fun downloadData() {
        AlertDialog.Builder(this)
            .setMessage("Please download CRM data first")
            .setPositiveButton(
                "Download",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    if (Constant.isNetworkConnected(this)) {
                        cmrDataViewModel?.getVehicleType()
                        cmrDataViewModel?.getActivityType()
                    } else {
                        msclass?.showMessage("No internet connection found")
                    }


                })
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun requestLocationPermission() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),
                Companion.MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 6000) {
            if (!isLocationEnabled(this)) {
                // notify user

                AlertDialog.Builder(this)
                    .setMessage("PLease enable your location from setting")
                    .setPositiveButton(
                        "Enable",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            startActivityForResult(
                                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                6000
                            )

                        })
                    .setNegativeButton("Cancel", null)
                    .show()
            } else {
                val sharedPreference: SharedPreference = SharedPreference(this)
                val gson = Gson()
                val jsonVehicle: String =
                    sharedPreference.getValueString(Constant.VEHICLE_LIST).toString()
                val typeVehicle: Type =
                    object : TypeToken<List<GetVehicleTypeResponseItem?>?>() {}.type
                val jsonActivity: String =
                    sharedPreference.getValueString(Constant.ACTIVITY_LIST).toString()
                val typeActivity: Type =
                    object : TypeToken<List<GetActivityTypeResponseItem?>?>() {}.type

                if (jsonVehicle != "null" && jsonActivity != "null") {
                    val Vehicle: List<GetVehicleTypeResponseItem> =
                        gson.fromJson(jsonVehicle, typeVehicle)
                    val activityList: List<GetActivityTypeResponseItem> =
                        gson.fromJson(jsonActivity, typeActivity)
                    if (Vehicle != null && Vehicle.size != 0 && activityList != null && activityList.size != 0) {
                        val intent = Intent(this, MyTravelActivity::class.java)
                        startActivityThis(intent)
                    } else {
                        downloadData()
                    }
                } else {
                    downloadData()

                }
            }
        }

        when (requestCode) {
            REQ_CODE_VERSION_UPDATE -> if (resultCode != RESULT_OK) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
//                Log.d("update listner", "Update flow failed! Result code: $resultCode")
                // If the update is cancelled or fails,
                // you can request to start the update again.
//                unregisterInstallStateUpdListener()
            }
        }

    }

    fun isLocationEnabled(context: Context): Boolean {
        var locationMode = 0
        val locationProviders: String
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            locationMode = try {
                Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
                return false
            }
            locationMode != Settings.Secure.LOCATION_MODE_OFF

        } else {
            locationProviders = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED
            )
            !TextUtils.isEmpty(locationProviders)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_about_us -> {
                Toast.makeText(applicationContext, "About us option coming soon", Toast.LENGTH_LONG)
                    .show()
                true
            }
            R.id.mnu_logout -> {
                /* Toast.makeText(applicationContext, "Logout option coming soon", Toast.LENGTH_LONG)
                     .show()*/
                CoroutineScope(Dispatchers.Main).launch {
                    llProgressBar.visibility = View.VISIBLE
                    tourList.clear()
                    tourList = wordViewModel.allWords() as MutableList<Word>
                    if (tourList != null || tourList.size != 0) {

                        for (item in tourList) {

                            if (item.uStatus != "1") {
                             /*   Log.e(
                                    "add_event",
                                    "onOptionsItemSelected: " + item.uId + " - status: " + item.uStatus
                                )*/
                                uploaded = false
                                msclass?.showMessage("Please end tour and upload data before logout !")
                                llProgressBar.visibility = View.GONE
                                break
                                return@launch
                            }
                        }
                        if (uploaded) {
                            logout()
                        }
                    } else {
                        logout()
                    }
                }

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        llProgressBar.visibility = View.GONE
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Are you sure you want to logout")
        alertDialog.setMessage("* Logout may wipe your travel and event data")
        alertDialog.setButton("Cancel") { dialog, which ->

        }
        alertDialog.setButton("Logout") { dialog, which ->
            // Write your code here to execute after dialog closed
            //        Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();


            CoroutineScope(Dispatchers.Main).launch {
                wordViewModel.clearAll()
                val sharedPreference: SharedPreference = SharedPreference(applicationContext)
                sharedPreference.save(Constant.IS_USER_LOGGED_IN, false)
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }
        }


        // Showing Alert Message
        alertDialog.show()

        /* CoroutineScope(Dispatchers.Main).launch {
             tourList = wordViewModel.allWords() as MutableList<Word>
             if (tourList != null || tourList.size != 0) {

                 for (item in tourList) {

                     if (item.uStatus != "1") {

                         msclass?.showMessage("Please end tour and upload data before logout !")
                         break
                         return@launch

                     }
                 }
                 val sharedPreference: SharedPreference = SharedPreference(applicationContext)
                 sharedPreference.save(Constant.IS_USER_LOGGED_IN, false)
                 startActivity(Intent(applicationContext, LoginActivity::class.java))
                 finish()


             } else {

             }
         }*/


    }


    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION = 66
    }
}