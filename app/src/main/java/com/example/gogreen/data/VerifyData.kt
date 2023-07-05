package com.example.gogreen.data

data class VerifyData(
    val certificateCID: String,
    val merkleLeaf: String,
    val merkleProof: List<String>,
    val tx: String,
    val txn_link: String,
    val verify: Boolean
)