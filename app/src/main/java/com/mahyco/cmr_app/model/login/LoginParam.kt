package com.mahyco.cmr_app.model.login

import com.google.gson.annotations.SerializedName

data class LoginParam(

	@field:SerializedName("UserName")
	val userName: String? = null,

	@field:SerializedName("DeviceID")
	val deviceID: String? = null,

	@field:SerializedName("Password")
	val password: String? = null
)
