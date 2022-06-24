package com.mahyco.cmr_app.view

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.core.BaseActivity
import com.mahyco.cmr_app.leegality.Response.LInvitee
import com.mahyco.cmr_app.leegality.Response.LeegalityMainResponse
import com.mahyco.isp.core.MainApplication
import kotlinx.android.synthetic.main.activity_d_c_e_sign.*


class DCESignActivity : BaseActivity() {

    lateinit var leegalityMainResponse: LeegalityMainResponse
    private val REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_d_c_e_sign)
        supportActionBar?.title = getString(R.string.app_name_long)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setUI()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setUI() {
        leegalityMainResponse = MainApplication.instance?.getLMainResponse()!!
        if (leegalityMainResponse.data.invitees != null) {
            var len = leegalityMainResponse.data.invitees.size
            for (i in 0 until len) {

                var lInvitee: LInvitee = leegalityMainResponse.data.invitees[i]

                if (i == 0) {
                    tv_title1.text = lInvitee.name
                    tv_link1.text = lInvitee.signUrl
                }

                if (i == 1) {
                    tv_title2.text = lInvitee.name
                    tv_link2.text = lInvitee.signUrl
                }

                if (i == 2) {
                    tv_title3.text = lInvitee.name
                    tv_link3.text = lInvitee.signUrl
                }

                if (i == 3) {
                    tv_title4.text = lInvitee.name
                    tv_link4.text = lInvitee.signUrl
                }
            }
        }
        else{
            showShortMessage("Invalid invitee details input")
        }

        btn_esign1.setOnClickListener(View.OnClickListener {
            val isAppInstalled = appInstalledOrNot("com.gspl.leegalityhelper")
            if (isAppInstalled) {
                val intent = Intent("com.gspl.leegalityhelper.Leegality")
                intent.putExtra("url", tv_link1.text)
                startActivityThis(intent)
            } else {
                showLongMessage(resources.getString(R.string.installapp))
                openLegality()
            }
        })

        btn_esign2.setOnClickListener(View.OnClickListener {
            val isAppInstalled = appInstalledOrNot("com.gspl.leegalityhelper")
            if (isAppInstalled) {
                val intent = Intent("com.gspl.leegalityhelper.Leegality")
                intent.putExtra("url", tv_link2.text)
                startActivityThis(intent)
            } else {
                showLongMessage(resources.getString(R.string.installapp))
                openLegality()
            }
        })

        btn_esign3.setOnClickListener(View.OnClickListener {
            val isAppInstalled = appInstalledOrNot("com.gspl.leegalityhelper")
            if (isAppInstalled) {
                val intent = Intent("com.gspl.leegalityhelper.Leegality")
                intent.putExtra("url", tv_link3.text)
                startActivityThis(intent)
            } else {
                showLongMessage(resources.getString(R.string.installapp))
                openLegality()
            }
        })

        btn_esign4.setOnClickListener(View.OnClickListener {
            val isAppInstalled = appInstalledOrNot("com.gspl.leegalityhelper")
            if (isAppInstalled) {
                val intent = Intent("com.gspl.leegalityhelper.Leegality")
                intent.putExtra("url", tv_link4.text)
                startActivityThis(intent)
            } else {
                showLongMessage(resources.getString(R.string.installapp))
                openLegality()
            }
        })
    }

    private fun openLegality() {
        val marketIntent =
                Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + "com.gspl.leegalityhelper")
                )
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(marketIntent)
    }


    private fun appInstalledOrNot(uri: String): Boolean {
        val pm = packageManager
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return false
    }
}