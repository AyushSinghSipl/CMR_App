package com.mahyco.cmr_app.view.upload

import android.content.Context
import android.os.Bundle
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.core.BaseActivity

class CMRDataUploadActivity : BaseActivity() {

    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c_m_r_data_upload)
        supportActionBar?.title = getString(R.string.cmr_data)
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
    }
}