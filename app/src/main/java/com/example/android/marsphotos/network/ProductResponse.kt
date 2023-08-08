package com.example.android.marsphotos.network

data class ProductResponse(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)