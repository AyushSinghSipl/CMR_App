package com.mahyco.cmr_app.model.getActivityType

import com.google.gson.annotations.SerializedName

data class GetActivityTypeResponse(

	@field:SerializedName("GetActivityTypeResponse")
	val getActivityTypeResponse: List<GetActivityTypeResponseItem?>? = null
)

data class GetActivityTypeResponseItem(

	@field:SerializedName("ActivityDescription")
	val activityDescription: String? = null,

	@field:SerializedName("ActivityCode")
	val activityCode: String? = null,

	@field:SerializedName("TrEntryDate")
	val trEntryDate: String? = null,

	@field:SerializedName("TrId")
	val trId: Int? = null
){
	override fun toString(): String {
		return activityDescription.toString()
	}
}
