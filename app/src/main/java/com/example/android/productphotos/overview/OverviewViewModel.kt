/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.productphotos.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.productphotos.network.ProductsApi
import com.example.android.productphotos.network.Product
import kotlinx.coroutines.launch

enum class ProductsApiStatus { LOADING, ERROR, DONE }

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<ProductsApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<ProductsApiStatus> = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of ProductPhoto
    // with new values
    private val _photos = MutableLiveData<List<Product>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val photos: LiveData<List<Product>> = _photos

    /**
     * Call getProductPhotos() on init so we can display status immediately.
     */
    init {
        getProductPhotos()
    }

    /**
     * Gets Product photos information from the Product API Retrofit service and updates the
     * [ProductsPhoto] [List] [LiveData].
     */
    private fun getProductPhotos() {

        viewModelScope.launch {
            _status.value = ProductsApiStatus.LOADING
            try {
                _photos.value = ProductsApi.retrofitService.getPhotos().products
                _status.value = ProductsApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ProductsApiStatus.ERROR
                _photos.value = listOf()
            }
        }
    }
}
