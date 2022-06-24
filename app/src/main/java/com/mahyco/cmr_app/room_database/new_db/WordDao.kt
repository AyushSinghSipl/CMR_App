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

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * The Room Magic is in this file, where you map a method call to an SQL query.
 *
 * When you are using complex data types, such as Date, you have to also supply type converters.
 * To keep this example basic, no types that require type converters are used.
 * See the documentation at
 * https://developer.android.com/topic/libraries/architecture/room.html#type-converters
 */

@Dao
interface WordDao {

    // The flow always holds/caches latest version of data. Notifies its observers when the
    // data has changed.
    @Query("SELECT * FROM word_table")
  suspend fun getAlphabetizedWords(): List<Word>

    @Query("SELECT * FROM word_table WHERE uDate =:date")
    suspend fun getCurrentDateTravels(date: String): List<Word>

    @Query("SELECT * FROM word_table WHERE uDate =:date AND uType =:type")
    fun getCurrentDateTravelType(date: String, type: String): Flow<List<Word>>

    @Query("SELECT * FROM word_table WHERE uDate =:date AND uType =:type")
  suspend  fun getCurrentDateTypeTravel(date: String, type: String): List<Word>

    @Query("SELECT * FROM word_table WHERE uType =:type")
    fun getTravelType(type: String): Flow<List<Word>>

    @Query("SELECT * FROM word_table WHERE uType !=:type")
    fun getNotTravelType(type: String): Flow<List<Word>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: Word)

    @Update(onConflict = OnConflictStrategy.REPLACE)
   suspend fun updateWord(word: Word)

    @Delete
    suspend  fun deleteWord(word: Word)

    @Query("DELETE FROM word_table WHERE uId = :id")
    suspend  fun deleteById(id: Long)

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()
}
