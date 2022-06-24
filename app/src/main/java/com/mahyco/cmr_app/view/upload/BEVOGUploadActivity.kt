package com.mahyco.cmr_app.view.upload

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.core.BaseActivity

class BEVOGUploadActivity : BaseActivity() {

    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_e_v_o_g_upload)
        supportActionBar?.title = getString(R.string.upload_be_vog)
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