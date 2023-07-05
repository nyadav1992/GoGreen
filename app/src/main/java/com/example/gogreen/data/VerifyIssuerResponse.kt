package com.example.gogreen.data

data class VerifyIssuerResponse(
    val `data`: VerifyData? = null,
    val status: Boolean,
    val message: String
)