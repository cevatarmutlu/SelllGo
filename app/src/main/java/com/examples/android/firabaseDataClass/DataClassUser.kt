package com.examples.android.firabaseDataClass

data class DataClassUser(
    val name:           String? = null,
    val surname:        String? = null,
    val companyName:    String? = null,
    val products:       List<String>? = null
)