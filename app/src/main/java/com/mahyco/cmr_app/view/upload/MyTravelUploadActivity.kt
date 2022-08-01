package com.mahyco.cmr_app.view.upload

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.example.android.roomwordssample.Word
import com.example.android.roomwordssample.WordViewModel
import com.example.android.roomwordssample.WordViewModelFactory
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.core.BaseActivity
import com.mahyco.cmr_app.core.DLog
import com.mahyco.cmr_app.core.Messageclass
import com.mahyco.cmr_app.viewmodel.CMRDataViewModel
import com.mahyco.isp.core.MainApplication
import kotlinx.android.synthetic.main.activity_my_travel_upload.*
import kotlinx.android.synthetic.main.activity_my_travel_upload.llProgressBar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MyTravelUploadActivity : BaseActivity() {

    lateinit var context: Context

    private val tourList: MutableList<Word> = ArrayList()
    var cmrDataViewModel: CMRDataViewModel? = null
    var msclass: Messageclass? = null
    var update = true

    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory(
            (application as MainApplication).repository,
            application as MainApplication
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_travel_upload)
        supportActionBar?.title = getString(R.string.upload_my_travel_data)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        context = this
        msclass = Messageclass(this)
        setUI()
        registerObserver()
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


        cmrDataViewModel?.loadingLiveData?.observe(this, androidx.lifecycle.Observer {
            if (it) {
                llProgressBar.visibility = View.VISIBLE
            } else {
                llProgressBar.visibility = View.GONE
            }
        })

//        In Case of error will show error in  toast message
        cmrDataViewModel!!.errorLiveData.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                showShortMessage(it)
                DLog.d("CMR errorLiveData :" + it)
            }
        })

        cmrDataViewModel!!.postTourEventsdata.observe(this, androidx.lifecycle.Observer {
            var result = it.toString()
            DLog.d("CMR DATA Activity : " + result)
            if (it.status == "Success") {
                update = false
                val events: MutableList<Word> = ArrayList()
                val tours: MutableList<Word> = ArrayList()

                val sd = SimpleDateFormat("MM/dd/yyyy")
                val currentDate = sd.format(Date())

/*
                for (item in tourList) {
                    if (item.uType == "add_event") {
                        events.add(item)
                    } else {
                        tours.add(item)
                    }
                }
*/
                lifecycleScope.launch {
                    val list = wordViewModel.allWords()

                    for (item in list) {
                        if (item.uDate != currentDate) {
                            wordViewModel.deleteWord(item)
                        } else {
                            item.uStatus = "1"
                            wordViewModel.update(item)
                            /*  if (events.size!= 0) {
                            for (event in events) {

                                for (tour in tours) {
                                    if (event.uDate == tour.uDate)
                                        item.uStatus = "1"
                                    wordViewModel.update(item)
                                }
                            }
                        }else{
                                item.uStatus = "1"
                            wordViewModel.update(item)
                        }*/
                        }
                    }
                }
                val alertDialog = AlertDialog.Builder(context).create()
                alertDialog.setTitle("Crop Monitor Report")
                alertDialog.setMessage("Data uploaded successfully")
                alertDialog.setCancelable(false)
                alertDialog.setButton("OK") { dialog, which ->
                    // Write your code here to execute after dialog closed
                    //        Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();

                    finish()
                    startActivity(this?.getIntent())
                }


                // Showing Alert Message
                if (alertDialog.isShowing){
                    alertDialog.dismiss()
                }
                alertDialog.show()
            } else {
                if (!it.errorMessage.toString().equals("null")) {
                    msclass?.showMessage(it.errorMessage.toString())
                }else{
                    msclass?.showMessage("Something went wrong")
                }
            }

        })


    }

    fun setUI() {

        tourList.clear()
        lifecycleScope.launch {
            val list = wordViewModel.allWords()
            for (item in list) {
                if (item.uStatus == "0") {
                    tourList.add(item)
                }
            }
            tv_records_count.text = tourList.size.toString()
        }

        btn_insurance_data_upload.setOnClickListener {
           /* if (update) {
                cmrDataViewModel?.postTourEventDataAPI(tourList)
            }*/
            lifecycleScope.launch {
                val list = wordViewModel.allWords()
                val listToUpdate : MutableList<Word> = ArrayList()
                for (item in list) {
                    if (item.uStatus == "0") {
                        listToUpdate.add(item)
                    }
                }
                cmrDataViewModel?.postTourEventDataAPI(listToUpdate)
            }

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (llProgressBar.visibility == View.VISIBLE){
            return false
        }else{
            return super.onTouchEvent(event)
        }


    }

}