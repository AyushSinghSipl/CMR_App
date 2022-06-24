package com.mahyco.cmr_app.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.android.roomwordssample.Word
import com.example.android.roomwordssample.WordViewModel
import com.example.android.roomwordssample.WordViewModelFactory
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.core.BaseActivity
import com.mahyco.cmr_app.view.upload.*
import com.mahyco.isp.core.MainApplication
import kotlinx.android.synthetic.main.activity_my_travel_upload.*
import kotlinx.android.synthetic.main.activity_upload_sync.*
import kotlinx.coroutines.launch
import java.util.ArrayList

class UploadSyncActivity : BaseActivity() {

    lateinit var context: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_sync)
        supportActionBar?.title = getString(R.string.data_upload_sync)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        context = this
        setUI()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setUI() {

        cd_cmr_data.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, CMRDataUploadActivity::class.java)
            startActivityThis(intent)
        })

        cd_tag_data.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, TagDataUploadActivity::class.java)
            startActivityThis(intent)
        })

        cd_insurance.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, InsuranceUploadActivity::class.java)
            startActivityThis(intent)
        })

        cd_my_travel.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MyTravelUploadActivity::class.java)
            startActivityThis(intent)
        })

        cd_be_vog.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, BEVOGUploadActivity::class.java)
            startActivityThis(intent)
        })
    }

}