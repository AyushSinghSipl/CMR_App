package com.mahyco.cmr_app.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject

class BaseResponse {

    var errorCodeData: String? = null
    var jsonObjectData: JsonObject? = null
    private var jsonArray: JsonArray? = null
    var errorMessageData: String? = null

    fun getJsonObject(): JsonObject? {
        return jsonObjectData
    }

    fun setJsonObject(jsonObject: JsonObject?) {
        this.jsonObjectData = jsonObject
    }

    fun getJsonArray(): JsonArray? {
        return jsonArray
    }

    fun setJsonArray(jsonArray: JsonArray?) {
        this.jsonArray = jsonArray
    }

    fun getErrorMessage(): String? {
        return errorMessageData
    }

    fun setErrorMessage(errorMessage: String?) {
        this.errorMessageData = errorMessage
    }

    fun getErrorCode(): String? {
        return errorCodeData
    }

    fun setErrorCode(errorCode: String?) {
        this.errorCodeData = errorCode
    }
}