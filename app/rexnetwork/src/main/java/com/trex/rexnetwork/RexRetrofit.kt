package com.trex.rexnetwork

import com.google.android.datatransport.BuildConfig
import com.google.firebase.perf.FirebasePerformance
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trex.rexnetwork.Constants.BASE_URL
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.UpdateTokenRequest
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface RexKtorServer {
    @POST("sendActionMessage")
    suspend fun sendOnlineMessage(
        @Body body: ActionMessageDTO,
    ): Response<Unit>

    //
    @POST("/updateDeviceFcmToken")
    suspend fun updateFcmToken(
        @Body body: UpdateTokenRequest,
    ): Response<Unit>

    @GET("/verifycode")
    suspend fun verifyCode(
        @Query("code") code: String,
        @Query("shopId") shopId: String,
        @Query("deviceId") deviceId: String,
    ): Response<Boolean>
}

// Firebase Performance Interceptor
class FirebasePerformanceInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val url = request.url.toString()
        val method = request.method

        val metric = FirebasePerformance.getInstance().newHttpMetric(url, method)
        metric.start()

        return try {
            chain.proceed(request).also { response ->
                with(metric) {
                    setHttpResponseCode(response.code)
                    setResponseContentType(response.header("Content-Type"))
                    response.body?.contentLength()?.let { setResponsePayloadSize(it) }
                    putAttribute("requestPath", request.url.encodedPath)
                    stop()
                }
            }
        } catch (e: Exception) {
            metric.putAttribute("error", e.message ?: "Unknown error")
            metric.stop()
            throw e
        }
    }
}

object RetrofitClient {
    private val moshi =
        Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    // Create Logging Interceptor
    private val loggingInterceptor =
        HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
        }

    // Create OkHttpClient with interceptors
    private val okHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(FirebasePerformanceInterceptor())
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                // Add headers interceptor
                val original = chain.request()
                val request =
                    original
                        .newBuilder()
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        // Add more headers if needed
                        .method(original.method, original.body)
                        .build()
                chain.proceed(request)
            }.connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    val getBuilder: RexKtorServer =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create()
}
