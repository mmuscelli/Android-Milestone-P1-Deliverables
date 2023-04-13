package com.example.connectedapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("repos/{owner}/{repo}/stats/participation")
    fun getContributionCounts(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ) : Call<ContributionCounts>
}