package com.mahyco.cmr_app.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mahyco.cmr_app.R

class GrowerIssueActivity : AppCompatActivity() {

    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grower_issue)
        supportActionBar?.title = getString(R.string.grower_issue)
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