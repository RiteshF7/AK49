package com.trex.rexnetwork.utils

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import com.trex.rexnetwork.domain.firebasecore.fcm.fcmrequestscreen.FcmRequestActivity

object ActivityUtils {
    const val INTENT_EXTRA_KEY = "intent_extra_key"

    fun <T : Parcelable> startActivity(
        context: Context,
        activity: Class<FcmRequestActivity>,
        extraData: T,
    ) {
        val intent = Intent(context, activity)
        intent.putExtra(INTENT_EXTRA_KEY, extraData)
        context.startActivity(intent)
    }

    fun startActivity(
        context: Context,
        activity: Class<FcmRequestActivity>,
    ) {
        val intent = Intent(context, activity)
        context.startActivity(intent)
    }

    inline fun <reified T : Parcelable> getExtraData(intent: Intent): T =
        intent.parcelable<T>(INTENT_EXTRA_KEY)
            ?: throw IllegalArgumentException("EXTRA ${T::class.simpleName} data is required!!")
}
