package com.bboluck.common.api.utils

object PhoneNumberUtil {
    fun replace(phoneNumber: String): String {
        val replacedPhoneNumber = phoneNumber.replace("-", "")

        return if (replacedPhoneNumber.startsWith("1")) {
            "0$replacedPhoneNumber"
        } else {
            replacedPhoneNumber
        }
    }
}
