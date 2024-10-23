package com.trex.rexnetwork.data

enum class Actions {
    ACTION_GET_PHONE_NUMBER,
    ACTION_GET_CONTACTS,
    ACTION_GET_CONTACTS_VIA_MESSAGE,
    ACTION_GET_DEVICE_INFO,
    ACTION_GET_UNLOCK_CODE,
    ACTION_GET_LOCATION,
    ACTION_GET_LOCATION_VIA_MESSAGE,
    ACTION_LOCK_DEVICE,
    ACTION_UNLOCK_DEVICE,
    ACTION_EMI_AUDIO_REMINDER,
    ACTION_EMI_SCREEN_REMINDER,
    ACTION_OFFLINE_LOCK,
    ACTION_OFFLINE_UNLOCK,
    ACTION_APP_UNLOCK,
    ACTION_CAMERA_LOCK,
    ACTION_CAMERA_UNLOCK,
    ACTION_SET_WALLPAPER,
    ACTION_REMOVE_WALLPAPER,
    ACTION_REBOOT_DEVICE,
    ACTION_CALL_LOCK,
    ACTION_CALL_UNLOCK,
    ACTION_LOCK_SCREEN,
    ACTION_RESET_PASSWORD,
    ACTION_REMOVE_DEVICE,
}

object ActionsMapper {
    // Convert enum to string
    fun fromEnumToString(action: Actions): String = action.name

    // Convert string to enum
    fun fromStringToEnum(actionString: String): Actions? =
        try {
            Actions.valueOf(actionString)
        } catch (e: IllegalArgumentException) {
            null // Return null if no matching enum is found
        }
}
