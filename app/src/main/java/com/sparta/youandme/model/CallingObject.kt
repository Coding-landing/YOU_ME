package com.sparta.youandme.model

import android.os.Parcelable
import android.telecom.Call
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class CallingObject(
    val id: String,
    @DrawableRes val imgId: Int,
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