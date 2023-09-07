package com.sparta.youandme.view.call

fun formatPhoneNumber(input: String): String {
    val cleanInput = input.replace("[^\\d]".toRegex(), "") // 숫자 이외의 문자 제거

    val formattedNumber = buildString {
        if (cleanInput.length >= 3) {
            append(cleanInput.substring(0, 3))
            if (cleanInput.length >= 7) {
                append("-")
                append(cleanInput.substring(3, 7))
                if (cleanInput.length >= 11) {
                    append("-")
                    append(cleanInput.substring(7, 11))
                } else {
                    append(cleanInput.substring(7))
                }
            } else {
                append(cleanInput.substring(3))
            }
        } else {
            append(cleanInput)
        }
    }

    return formattedNumber
}