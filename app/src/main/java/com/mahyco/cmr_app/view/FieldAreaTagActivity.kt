package com.mahyco.cmr_app.view

import android.content.Context
import android.os.Bundle
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.core.BaseActivity

class FieldAreaTagActivity : BaseActivity() {

    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_field_area_tag)
        supportActionBar?.title = getString(R.string.field_area_tagging)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        context = this
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setUI() {
    }
}