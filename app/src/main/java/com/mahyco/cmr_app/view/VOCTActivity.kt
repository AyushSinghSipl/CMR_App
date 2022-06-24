package com.mahyco.cmr_app.view

import android.content.Context
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.core.BaseActivity
import com.mahyco.cmr_app.utils.WebViewClientImpl
import kotlinx.android.synthetic.main.activity_v_o_c_t.*

class VOCTActivity : BaseActivity() {

    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_v_o_c_t)
        supportActionBar?.title = getString(R.string.voct)
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
        val webSettings: WebSettings = webView.getSettings()
        webSettings.javaScriptEnabled = true
        // disable Web SQL
        // disable Web SQL
        webView.setWebChromeClient(WebChromeClient())

        val webViewClient = WebViewClientImpl(this)
        webView.setWebViewClient(webViewClient)
        //webview.loadUrl(urlWebView+ "userCode=" + usercode);
        //webview.loadUrl(urlWebView+ "userCode=" + usercode);
        //webView.loadUrl("https://besurvey.mahyco.com/Login/IndexMobile?tfacode=$usercode")
        webView.loadUrl("https://besurvey.mahyco.com/Login/IndexMobile?tfacode=97190469")

    }
}