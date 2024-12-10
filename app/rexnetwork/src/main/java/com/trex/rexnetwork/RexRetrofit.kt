package com.trex.rexnetwork

import android.util.Log
import com.google.android.datatransport.BuildConfig
import com.google.firebase.perf.FirebasePerformance
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trex.rexnetwork.Constants.BASE_URL
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.UpdateTokenRequest
import com.trex.rexnetwork.domain.repositories.DeviceRegistration
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
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

sealed class NetworkResult<out T> {
    data class Success<T>(
        val data: T,
    ) : NetworkResult<T>()

    data class Error(
        val exception: Exception,
    ) : NetworkResult<Nothing>()
}

interface RexKtorServer {
    @POST("sendActionMessage")
    suspend fun sendOnlineMessage(
        @Body body: ActionMessageDTO,
    ): Response<Unit>

    @POST("/presence/start")
    suspend fun startPresenceMonitoring(
        @Body body: DeviceRegistration,
    ): Response<Map<String, String>>

    @POST("/presence/stop")
    suspend fun stopPresenceMonitoring(
        @Query("deviceId") deviceId: String,
    ): Response<Map<String, String>>

    @POST("/updateDeviceFcmToken")
    suspend fun updateFcmToken(
        @Body body: UpdateTokenRequest,
    ): Response<Unit>

    data class FileUrlResponse(val url: String)
    @GET("/api/apk/url")
    suspend fun getRascApkUrl(): Response<FileUrlResponse>

    @GET("/api/debug/apk/url")
    suspend fun getDebugRascApkUrl(): Response<FileUrlResponse>

    @GET("/verifycode")
    suspend fun verifyCode(
        @Query("code") code: String,
        @Query("shopId") shopId: String,
        @Query("deviceId") deviceId: String,
    ): Response<Boolean>

    @GET("/updateMasterCode")
    suspend fun updateMasterCode(
        @Query("shopId") shopId: String,
        @Query("deviceId") deviceId: String,
    ): Response<String>

    @GET("/getUnlockCode")
    suspend fun getUnlockCode(
        @Query("shopId") shopId: String,
        @Query("deviceId") deviceId: String,
    ): Response<String>
}

// Custom logging interceptor
class RequestLoggingInterceptor : Interceptor {
    companion object {
        private const val TAG = "NetworkCall"
    }

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val requestUrl = request.url.toString()
        val requestBody = request.body?.toString() ?: "empty body"
        val requestMethod = request.method
        val requestHeaders = request.headers

        Log.d(
            TAG,
            """
            🌐 REQUEST:
            URL: $requestUrl
            Method: $requestMethod
            Headers: $requestHeaders
            Body: $requestBody
            """.trimIndent(),
        )

        val startTime = System.currentTimeMillis()
        val response = chain.proceed(request)
        val endTime = System.currentTimeMillis()

        val responseBody =
            try {
                response.peekBody(Long.MAX_VALUE).string()
            } catch (e: Exception) {
                "Could not read response body: ${e.message}"
            }

        Log.d(
            TAG,
            """
            📥 RESPONSE:
            URL: $requestUrl
            Time taken: ${endTime - startTime}ms
            Code: ${response.code}
            Message: ${response.message}
            Body: $responseBody
            """.trimIndent(),
        )

        return response
    }
}

suspend fun <T> safeApiCall(
    tag: String,
    apiCall: suspend () -> Response<T>,
): NetworkResult<T> =
    try {
        val response = apiCall()
        if (response.isSuccessful) {
            Log.d(tag, "✅ API call successful: ${response.code()}")
            NetworkResult.Success(response.body()!!)
        } else {
            Log.e(tag, "❌ API call failed with code: ${response.code()}")
            Log.e(tag, "Error body: ${response.errorBody()?.string()}")
            NetworkResult.Error(Exception("API call failed with code: ${response.code()}"))
        }
    } catch (e: SocketTimeoutException) {
        Log.e(tag, "⏰ Timeout error: ${e.message}")
        NetworkResult.Error(e)
    } catch (e: Exception) {
        Log.e(tag, "💥 Error during API call: ${e.message}")
        Log.e(tag, "Stack trace: ${e.stackTrace.joinToString("\n")}")
        NetworkResult.Error(e)
    }

class FirebasePerformanceInterceptor : Interceptor {
    companion object {
        private const val TAG = "FirebasePerf"
    }

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val url = request.url.toString()
        val method = request.method

        Log.d(TAG, "📊 Starting Firebase performance tracking for $url")
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
                Log.d(TAG, "✅ Firebase performance tracking completed for $url")
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error during Firebase performance tracking: ${e.message}")
            metric.putAttribute("error", e.message ?: "Unknown error")
            metric.stop()
            throw e
        }
    }
}

object RetrofitClient {
    private const val TAG = "RetrofitClient"

    private val moshi =
        Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    private val loggingInterceptor =
        HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
        }

    private val okHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(RequestLoggingInterceptor()) // Add custom logging interceptor
            .addInterceptor(FirebasePerformanceInterceptor())
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()
                val request =
                    original
                        .newBuilder()
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
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

    suspend fun verifyCodeSafely(
        code: String,
        shopId: String,
        deviceId: String,
    ): NetworkResult<Boolean> {
        Log.d(TAG, "📤 Calling verifyCode - code: $code, shopId: $shopId, deviceId: $deviceId")
        return safeApiCall(TAG) {
            getBuilder.verifyCode(code, shopId, deviceId)
        }
    }

    suspend fun sendOnlineMessageSafely(message: ActionMessageDTO): NetworkResult<Unit> {
        Log.d(TAG, "📤 Sending online message: $message")
        return safeApiCall(TAG) {
            getBuilder.sendOnlineMessage(message)
        }
    }

    suspend fun updateFcmTokenSafely(request: UpdateTokenRequest): NetworkResult<Unit> {
        Log.d(TAG, "📤 Updating FCM token: $request")
        return safeApiCall(TAG) {
            getBuilder.updateFcmToken(request)
        }
    }
}
