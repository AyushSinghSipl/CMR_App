package com.mahyco.cmr_app.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.core.BaseActivity

class GrowerInfoActivity : BaseActivity() {

    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grower_info)
        supportActionBar?.title = getString(R.string.grower_info)
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