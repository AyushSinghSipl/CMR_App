package com.mahyco.cmr_app.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.core.BaseActivity
import com.mahyco.cmr_app.core.DLog
import com.mahyco.cmr_app.viewmodel.CMRDataViewModel
import kotlinx.android.synthetic.main.activity_download.*

class DownloadActivity : BaseActivity() {

    lateinit var context: Context
    var cmrDataViewModel: CMRDataViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
        supportActionBar?.title = getString(R.string.download_grower_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        registerObserver()
        context = this
        setUI()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
                //showShortMessage(it)
                //startScanning()
                DLog.d("CMR errorLiveData :" + it)
            }
        })


        cmrDataViewModel!!.cmrDataMutableLiveData.observe(this, Observer {
            var result = it.toString()
            DLog.d("CMR DATA Response : " + result)
        })

    }

    fun setUI() {
        edt_emp_code_dl.setText("97180401")
        edt_tfa_code_dl.setText("97180401")

        var userCode = edt_tfa_code_dl.text.toString()
        val staffCode = edt_emp_code_dl.text.toString()


        /* https://cmrapi.mahyco.com/api/cmr/getCMRDataForValidation API Call*/
        /*Implemented and Tested 31st March 2021*/
        /*if (isNetworkAvailable(this@DownloadActivity)) {
            cmrDataViewModel!!.getCMRDataForValAPI(
                "123",
                "2015",
                staffCode,
                "pending",
                "xyzstring",
                userCode
            )
        } else {
            showLongMessage(resources.getString(R.string.no_internet))
        }*/

        /*https://cmrapi.mahyco.com/api/cmr/getCMRtendata*/
        /*Implemented and Tested 31st March 2021*/
        /*if (isNetworkAvailable(this@DownloadActivity)) {
            cmrDataViewModel!!.getCMRTenDataAPI(
                "123",
                "2015",
                staffCode,
                "pending",
                "xyzstring",
                userCode
            )
        } else {
            showLongMessage(resources.getString(R.string.no_internet))
        }*/

        /*https://cmrapi.mahyco.com/api/cmr/getCMRtwentlydata*/
        /*Implemented and Tested 31st March 2021*/
        /*if (isNetworkAvailable(this@DownloadActivity)) {
            cmrDataViewModel!!.getCMRTwentyDataAPI(
                "123",
                "2015",
                staffCode,
                "pending",
                "xyzstring",
                userCode
            )
        } else {
            showLongMessage(resources.getString(R.string.no_internet))
        }*/

        /*https://cmrapi.mahyco.com/api/cmr/getCMRthirtydata*/
        /*Implemented and Tested 31st March 2021*/
        /*if (isNetworkAvailable(this@DownloadActivity)) {
            cmrDataViewModel!!.getCMRThirtyDataAPI(
                "123",
                "2015",
                staffCode,
                "pending",
                "xyzstring",
                userCode
            )
        } else {
            showLongMessage(resources.getString(R.string.no_internet))
        }*/

        /*https://cmrapi.mahyco.com/api/cmr/moStaffwiserGrowerReport*/
        /*Implemented and Tested 31st March 2021*/
        if (isNetworkAvailable(this@DownloadActivity)) {
            cmrDataViewModel!!.getMOStaffWiserGrowerReport(
                "123",
                "2015",
                staffCode,
                "pending",
                "xyzstring",
                userCode,
                "123",
                "97180401"
            )
        } else {
            showLongMessage(resources.getString(R.string.no_internet))
        }


    }
}