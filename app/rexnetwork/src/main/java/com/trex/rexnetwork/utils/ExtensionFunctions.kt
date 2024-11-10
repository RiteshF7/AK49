package com.trex.rexnetwork.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Parcelable
import com.trex.rexnetwork.data.Actions

const val INTENT_EXTRA_KEY = "intent_extra_key"

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? =
    when {
        SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else ->
            @Suppress("DEPRECATION")
            getParcelableExtra(key)
    }

fun <T : Parcelable> Context.startMyActivity(
    activityClass: Class<out Activity>,
    extraData: T,
    isNewTask: Boolean = true,
) {
    val intent =
        Intent(this, activityClass).apply {
            putExtra(INTENT_EXTRA_KEY, extraData)
            if (isNewTask) {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }
    this.startActivity(intent)
}

// Overloaded function to start any activity without extra data
fun Context.startMyActivity(activityClass: Class<out Activity>) {
    val intent = Intent(this, activityClass)
    this.startActivity(intent)
}

// Inline function to retrieve extra data of any Parcelable type
inline fun <reified T : Parcelable> Intent.getExtraData(): T =
    this.parcelable<T>(INTENT_EXTRA_KEY)
        ?: throw IllegalArgumentException("EXTRA ${T::class.simpleName} data is required!!")

fun Actions.isGetRequest(): Boolean = this.name.contains("GET")
