package com.sparta.youandme.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CallingObject(
    val id: String,
    val imgId: Uri?,
    val name: String,
    val mobileNumber: String,
    val email: String,
    val snsAddress: String,
    var mbti: String = "",
    var nickName: String = "",
    var blogAddress: String = "",
    var isLiked: Boolean = false,
    var type: Int
) : Parcelable, Cloneable {
    public override fun clone(): Any {
        return CallingObject(id, imgId, name, mobileNumber, email, snsAddress, mbti, nickName, blogAddress, isLiked, type)
    }
}