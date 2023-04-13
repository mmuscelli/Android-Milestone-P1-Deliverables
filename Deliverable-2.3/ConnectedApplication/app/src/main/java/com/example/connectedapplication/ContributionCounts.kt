package com.example.connectedapplication

import com.google.gson.annotations.SerializedName

data class ContributionCounts(
    @SerializedName("all"   ) var all   : MutableList<Int> = mutableListOf(),
    @SerializedName("owner" ) var owner : MutableList<Int> = mutableListOf()
)
