package com.mahyco.cmr_app.model.getEventTourData

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class TourEventParam(

	@field:SerializedName("TourEventParam")
	val tourEventParam: List<TourEventParamItem?>? = null
)

data class TourEventParamItem(

	@field:SerializedName("uStartDateTime")
	val uStartDateTime: String? = "",

	@field:SerializedName("uKmImageEvent")
	val uKmImageEvent: String? = "",

	@field:SerializedName("uTourId")
	val uTourId: Int? = null,

	@field:SerializedName("uStatus")
	val uStatus: String? = "",

	@field:SerializedName("uVehicleId")
	val uVehicleId: Int? = null,

	@field:SerializedName("uReadingStart")
	val uReadingStart: Int? = null,

	@field:SerializedName("uEntryBy")
	val uEntryBy: String? = "",

	@field:SerializedName("uId")
	val uId: Int? = null,

	@field:SerializedName("uEventDateTime")
	val uEventDateTime: String? = "",

	@field:SerializedName("uImageEnd")
	val uImageEnd: String? = "",

	@field:SerializedName("uStartLatLng")
	val uStartLatLng: String? = "",

	@field:SerializedName("uDate")
	val uDate: String? = "",

	@field:SerializedName("uImageStart")
	val uImageStart: String? = "",

	@field:SerializedName("uReadingEnd")
	val uReadingEnd: Int? = null,

	@field:SerializedName("uEventLatLng")
	val uEventLatLng: String? = "",

	@field:SerializedName("uEndDateTime")
	val uEndDateTime: String? = "",

	@field:SerializedName("uEventId")
	val uEventId: Int? = null,

	@field:SerializedName("uEventDescription")
	val uEventDescription: String? = "",

	@field:SerializedName("uType")
	val uType: String? = "",

	@field:SerializedName("uEndLatLng")
	val uEndLatLng: String? = "",
	@ColumnInfo(name = "uServerId") val uServerId: String,
	@ColumnInfo(name = "uEventReading") val uEventReading: String,
)
