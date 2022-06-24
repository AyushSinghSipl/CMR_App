package com.mahyco.cmr_app.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.core.BaseActivity

class DownloadCMRDataActivity : BaseActivity() {

    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_c_m_r_data)
        supportActionBar?.title = getString(R.string.download_cmr_data)
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