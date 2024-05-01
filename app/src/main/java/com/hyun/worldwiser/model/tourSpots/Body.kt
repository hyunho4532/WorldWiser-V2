package com.hyun.worldwiser.model.tourSpots

data class Body(
    val items: Items,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)