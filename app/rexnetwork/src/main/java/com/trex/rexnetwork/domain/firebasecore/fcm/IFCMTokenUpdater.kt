package com.trex.rexnetwork.domain.firebasecore.fcm

import android.content.Context
import android.util.Log
import com.trex.rexnetwork.utils.SharedPreferenceManager

interface IFCMTokenUpdater {
    fun updateFirestoreFCMToken(token: String)
}

abstract class FCMTokenUpdater(
    context: Context,
) : IFCMTokenUpdater {
    protected val mshardPref = SharedPreferenceManager(context)
    protected val shopId = mshardPref.getShopId()

    val success = fun() {
        Log.i("", "fcm token updateSuccess on firestore!")
    }

    val failed = fun(error: Exception) {
        Log.i("", "updateFailure: fcm token failed on firestore!$error")
    }
}
