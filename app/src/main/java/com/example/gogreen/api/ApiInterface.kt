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
    suspend fun getStationInfo(@Query("station_id") id: String) : StationInfo

    @GET("chargingInfo")
    suspend fun getProfileInfo(@Query("my_wallet_address") my_wallet_address: String) : StationInfo

    @POST("chargingInfo/start_charging")
    suspend fun startCharging(@Body req: StartChargingRequest) : StartChargingResponse

    @POST("chargingInfo/stop_charging")
    suspend fun stopCharging(@Body req: StopChargingRequest) : StopChargingResponse

    @POST("chargingInfo/pay_charging")
    suspend fun payCharging(@Body req: PayChargingRequest) : StopChargingResponse

    @GET("history")
    suspend fun getHistoryData(@Query("my_wallet_address") my_wallet_address: String) : HistoryData

    @GET("chargingInfo/charging_progress")
    suspend fun getProgress(@Query("my_wallet_address") my_wallet_address: String, @Query("station_id") station_id: String) : StartChargingResponse

    @GET("verify")
    suspend fun verifyIssuer(@Query("station_id") station_id: String) : VerifyIssuerResponse

}