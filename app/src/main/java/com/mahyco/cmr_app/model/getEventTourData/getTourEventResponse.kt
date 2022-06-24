package com.mahyco.cmr_app.model.getEventTourData

import com.google.gson.annotations.SerializedName

data class GetTourEventResponse(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("Response")
	val response: Response? = null,

	@field:SerializedName("ErrorMessage")
	val errorMessage: Any? = null
)

data class Response(

	@field:SerializedName("InsertedTour")
	val insertedTour: Int? = null,

	@field:SerializedName("InsertedEvent")
	val insertedEvent: Int? = null,

	@field:SerializedName("EndedTour")
	val EndedTour: Int? = null,

	@field:SerializedName("RecordUploaded")
	val recordUploaded: Int? = null
)
