package com.trex.rexnetwork

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trex.rexnetwork.Constants.BASE_URL
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.NewDevice
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.POST

interface RexKtorServer {
    @POST("sendActionMessage")
    suspend fun sendOnlineMessage(
        @Body body: ActionMessageDTO,
    ): Response<Unit>

    //
    @POST("/regdevice")
    suspend fun registerNewDevice(
        @Body body: NewDevice,
    ): Response<Unit>
}

object RetrofitClient {
    private val moshi =
        Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    val getBuilder: RexKtorServer =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create()
}
