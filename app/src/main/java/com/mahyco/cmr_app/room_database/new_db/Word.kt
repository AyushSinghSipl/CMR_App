/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.roomwordssample

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

/**
 * A basic class representing an entity that is a row in a one-column database table.
 *
 * @ Entity - You must annotate the class as an entity and supply a table name if not class name.
 * @ PrimaryKey - You must identify the primary key.
 * @ ColumnInfo - You must supply the column name if it is different from the variable name.
 *
 * See the documentation for the full rich set of annotations.
 * https://developer.android.com/topic/libraries/architecture/room.html
 */

@Entity(tableName = "word_table")
data class Word(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "uId") val uId: Int,
    @ColumnInfo(name = "uStartDateTime") val uStartDateTime: String,
    @ColumnInfo(name = "uEndDateTime") val uEndDateTime: String,
    @ColumnInfo(name = "uEventDateTime") val uEventDateTime: String,
    @ColumnInfo(name = "uVehicleId") val uVehicleId: String,
    @ColumnInfo(name = "uVehicleType") val uVehicleType: String,
    @ColumnInfo(name = "uAddress") val uAddress: String,
    @ColumnInfo(name = "uStartLat") val uStartLat: String,
    @ColumnInfo(name = "uStartLng") val uStartLng: String,
    @ColumnInfo(name = "uEndLat") val uEndLat: String,
    @ColumnInfo(name = "uEndLng") val uEndLng: String,
    @ColumnInfo(name = "uEventLat") val uEventLat: String,
    @ColumnInfo(name = "uEventLng") val uEventLng: String,
    @ColumnInfo(name = "uReadingStart") val uKmReadingStart: String,
    @ColumnInfo(name = "uImageStart") val uKmImageStart: String,
    @ColumnInfo(name = "uReadingEnd") val uKmReadingEnd: String,
    @ColumnInfo(name = "uImageEnd") val uKmImageEnd: String,
    @ColumnInfo(name = "uKmImageEvent") val uKmImageEvent: String,
    @ColumnInfo(name = "uStatus") var uStatus: String,
    @ColumnInfo(name = "uType") val uType: String,
    @ColumnInfo(name = "uEventType") val uEventType: String,
    @ColumnInfo(name = "uEventId") val uEventId: String,
    @ColumnInfo(name = "uTourId") val uTourId: String,
    @ColumnInfo(name = "uEventDescription") val uEventDescription: String,
    @ColumnInfo(name = "uDate") val uDate: String,
    @ColumnInfo(name = "uServerId") val uServerId: String,
    @ColumnInfo(name = "uEventReading") val uEventReading: String,
)
