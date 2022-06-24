package com.mahyco.cmr_app.model.login

import com.google.gson.annotations.SerializedName

data class LoginResponseModel(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("Response")
	val response: Response? = null,

	@field:SerializedName("ErrorMessage")
	val errorMessage: Any? = null
)

data class Response(

	@field:SerializedName("UserCode")
	val userCode: Any? = null,

	@field:SerializedName("Token")
	val token: String? = null,

	@field:SerializedName("Name")
	val name: String? = null,

	@field:SerializedName("IsValid")
	val isValid: Boolean? = null
)
