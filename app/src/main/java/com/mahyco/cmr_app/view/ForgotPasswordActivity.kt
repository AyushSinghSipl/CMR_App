package com.mahyco.cmr_app.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mahyco.cmr_app.R

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        supportActionBar?.title = getString(R.string.app_name_long)
    }
}