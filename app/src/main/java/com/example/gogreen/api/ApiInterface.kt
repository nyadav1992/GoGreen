package com.example.gogreen.api

import com.example.gogreen.data.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
/*    @POST("mobile_api.php")
    suspend fun loginUser(@Query("action") login: String, @Body q: LoginRequest) : Response<LogInResponse>

    @GET("categories")GET
    suspend fun getCategory(@Query("page") login: String, @Query("per_page") perPage: String) : Response<DummyResponse>*/

    @GET("chargingInfo")
    suspend fun getStationInfo(@Query("id") id: String) : StationInfo

    @POST("chargingInfo/start_charging")
    suspend fun startCharging(@Body req: StartChargingRequest) : StartChargingResponse

    @GET("history")
    suspend fun getHistoryData(@Query("my_id") id: String) : HistoryData

}