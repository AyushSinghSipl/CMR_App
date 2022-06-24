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

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */
class WordRepository(private val wordDao: WordDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
   suspend fun  allWords(): List<Word> = wordDao.getAlphabetizedWords()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(word: Word) {
        wordDao.updateWord(word)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteById(id: Long) {
        wordDao.deleteById(id)
    }




    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteWord(word: Word) {
        wordDao.deleteWord(word)
    }

   suspend fun getCurrentTravel(date: String): List<Word> {
        return wordDao.getCurrentDateTravels(date)
    }

    fun getCurrentDateTravelType(date: String,type:String): Flow<List<Word>> {
        return wordDao.getCurrentDateTravelType(date, type)
    }

   suspend fun getCurrentDateTypeTravel(date: String,type:String): List<Word> {
        return wordDao.getCurrentDateTypeTravel(date, type)
    }
    fun getTravelType(type:String): Flow<List<Word>> {
        return wordDao.getTravelType(type)
    }

    fun getNotTravelType(type:String): Flow<List<Word>> {
        return wordDao.getNotTravelType(type)
    }

   suspend fun clearAll() {
        wordDao.deleteAll()
    }
}
