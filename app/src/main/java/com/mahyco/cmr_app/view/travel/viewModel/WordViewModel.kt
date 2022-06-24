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

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mahyco.cmr_app.api.IDataServiceCMR
import com.mahyco.isp.core.MainApplication
import com.mahyco.isp.repositories.RetrofitApiClient
import com.mahyco.isp.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */

class WordViewModel(private val repository: WordRepository, application: MainApplication) : BaseViewModel(application) {
    private var mContext: Context = application

    var apiClient = RetrofitApiClient.getAPIClient()

    private var  iServiceISP = apiClient.create(IDataServiceCMR::class.java)

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    suspend fun  allWords(): List<Word> = repository.allWords()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(word: Word) = viewModelScope.launch {
        try {
            repository.insert(word)
        }catch (e:Exception){
            Log.e("Room", "insert: "+e.message )
        }

    }
    fun update(word: Word) = viewModelScope.launch {
        try {
            repository.update(word)
        }catch (e:Exception){
            Log.e("Room", "update: "+e.message )
        }
    }


    fun deleteWord(word: Word) = viewModelScope.launch {
        try {
            repository.deleteWord(word)
        }catch (e:Exception){
            Log.e("Room", "update: "+e.message )
        }

    }

    fun deleteById(id:Long) = viewModelScope.launch {
        try {
            repository.deleteById(id)
        }catch (e:Exception){
            Log.e("Room", "update: "+e.message )
        }

    }



    fun getTravelType(type:String): LiveData<List<Word>> {
        return  repository.getTravelType(type).asLiveData()
    }

    fun getNotTravelType(type:String): LiveData<List<Word>> {
        return  repository.getNotTravelType(type).asLiveData()
    }



    suspend fun getCurrentTravel(currentDate: String): List<Word> {
      return  repository.getCurrentTravel(currentDate)
    }
    fun getCurrentDateTravelType(date: String,type:String): LiveData<List<Word>> {
      return  repository.getCurrentDateTravelType(date, type).asLiveData()
    }

   suspend fun getCurrentDateTypeTravel(date: String,type:String): List<Word> {
      return  repository.getCurrentDateTypeTravel(date, type)
    }

   suspend fun clearAll() {
       repository.clearAll()
    }
}

class WordViewModelFactory(private val repository: WordRepository,val application: MainApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordViewModel(repository,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


