package com.mahyco.cmr_app.view

import android.content.Context
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.core.BaseActivity
import com.mahyco.cmr_app.view.adapters.ViewPagerAdapterUploadSync
import kotlinx.android.synthetic.main.activity_data_upload_sync.*

class DataUploadSyncActivity : BaseActivity() {

    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_upload_sync)
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
        //setSupportActionBar(toolbar)

        val adapter = ViewPagerAdapterUploadSync(supportFragmentManager)
        adapter.addFragment(UploadCMRDataFragment(), "CMR DATA")
        adapter.addFragment(UploadCMRDataFragment(), "TAG DATA")
        adapter.addFragment(UploadCMRDataFragment(), "INSURANCE")
        adapter.addFragment(UploadCMRDataFragment(), "My TRAVEL")
        adapter.addFragment(UploadCMRDataFragment(), "BE-VOG")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}