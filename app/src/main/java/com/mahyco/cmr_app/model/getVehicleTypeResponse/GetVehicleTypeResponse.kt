package com.mahyco.cmr_app.model.getVehicleTypeResponse

import com.google.gson.annotations.SerializedName

data class GetVehicleTypeResponse(
	val getVehicleTypeResponse: List<GetVehicleTypeResponseItem?>? = null
)

data class GetVehicleTypeResponseItem(
	@field:SerializedName("VehicleCode")
	val vehicleCode: String? = null,

	@field:SerializedName("TrEntryDate")
	val trEntryDate: String? = null,

	@field:SerializedName("VehicleDescription")
	val vehicleDescription: String? = null,

	@field:SerializedName("TrId")
	val trId: Int? = null



){
	override fun toString(): String {
		return vehicleDescription.toString()
	}
}

