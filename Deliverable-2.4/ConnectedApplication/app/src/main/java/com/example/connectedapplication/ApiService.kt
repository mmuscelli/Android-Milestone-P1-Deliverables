package com.example.connectedapplication

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("repos/{owner}/{repo}/stats/participation")
    suspend fun getContributionCounts(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ) : ContributionCounts
}