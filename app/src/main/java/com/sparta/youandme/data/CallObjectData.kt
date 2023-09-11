package com.sparta.youandme.data

import android.net.Uri
import androidx.annotation.DrawableRes
import com.sparta.youandme.R
import com.sparta.youandme.model.CallingObject
import com.sparta.youandme.model.ViewType
import java.util.UUID

object CallObjectData {
    val list: List<CallingObject>
        get() = _list
    private val _list = arrayListOf(
        CallingObject(
            id = UUID.randomUUID().toString(),
            imgId = drawableToUri(R.drawable.bg_hyerin),
            name = "박혜린",
            mobileNumber = "010-5684-8994",
            email = "thwn1012@gmail.com",
            snsAddress = "instagram.com/hyerin0820",
            mbti = "ISTJ",
            nickName = "cat",
            blogAddress = "https://velog.io/@thwn1012",
            type = ViewType.DEFAULT_POSITION
        ),
        CallingObject(
            id = UUID.randomUUID().toString(),
            imgId = drawableToUri(R.drawable.bg_junsun),
            name = "소준선",
            mobileNumber = "010-6321-7998",
            email = "sojh8908@gmail.com",
            snsAddress = "",
            mbti = "ISTP",
            blogAddress = "https://junseon98.tistory.com/",
            type = ViewType.DEFAULT_POSITION
        ),
        CallingObject(
            id = UUID.randomUUID().toString(),
            imgId = drawableToUri(R.drawable.bg_minjong),
            name = "김민종",
            mobileNumber = "010-8947-7397",
            email = "alswhddkdl@gmail.com",
            snsAddress = "https://www.facebook.com/profile.php?id=100006500445244",
            mbti = "ISTJ",
            nickName = "AAApple",
            blogAddress = "https://aaapple.tistory.com/",
            type = ViewType.DEFAULT_POSITION
        ),
        CallingObject(
            id = UUID.randomUUID().toString(),
            imgId = drawableToUri(R.drawable.bg_seungchul),
            name = "신승철",
            mobileNumber = "010-9542-7359",
            email = "blueskyroad@naver.com",
            snsAddress = "https://www.facebook.com/profile.php?id=100003286841954",
            mbti = "ISFP",
            nickName = "깜시",
            blogAddress = "https://velog.io/@tlstmdcjfekt",
            type = ViewType.DEFAULT_POSITION
        ),
        CallingObject(
            id = UUID.randomUUID().toString(),
            imgId = drawableToUri(R.drawable.bg_sunho),
            name = "정선호",
            mobileNumber = "010-9027-4209",
            email = "sunho512@gmail.com",
            snsAddress = "https://www.facebook.com/profile.php?id=100004828384746",
            mbti = "ISFP",
            nickName = "프리퍼",
            blogAddress = "https://sunho0226.tistory.com/",
            type = ViewType.DEFAULT_POSITION
        ),
        CallingObject(
            id = UUID.randomUUID().toString(),
            imgId = drawableToUri(R.drawable.bg_talent_1),
            name = "권은비",
            mobileNumber = "010-1234-1212",
            email = "silver_rain@gmail.com",
            snsAddress = "https://www.instagram.com/silver_rain.__/",
            mbti = "ENFP",
            nickName = "대장토끼",
            type = ViewType.DEFAULT_POSITION
        ),
        CallingObject(
            id = UUID.randomUUID().toString(),
            imgId = drawableToUri(R.drawable.bg_talent_3),
            name = "황민현",
            mobileNumber = "010-8894-4988",
            email = "sisun@gmail.com",
            snsAddress = "https://www.instagram.com/optimushwang/",
            mbti = "ESFJ",
            nickName = "황제",
            type = ViewType.DEFAULT_POSITION

        ),
        CallingObject(
            id = UUID.randomUUID().toString(),
            imgId = drawableToUri(R.drawable.bg_talent_4),
            name = "전원우",
            mobileNumber = "010-5889-4885",
            email = "ddifj@gmail.com",
            snsAddress = "https://www.instagram.com/everyone_woo/",
            mbti = "INFJ",
            nickName = "워누",
            type = ViewType.DEFAULT_POSITION
        ),
        CallingObject(
            id = UUID.randomUUID().toString(),
            imgId = drawableToUri(R.drawable.bg_talent_5),
            name = "강해린",
            mobileNumber = "010-2332-3443",
            email = "haerin@gmail.com",
            snsAddress = "https://www.instagram.com/newjeans_official/",
            mbti = "ISTP",
            nickName = "강해린 이상하다",
            type = ViewType.DEFAULT_POSITION
        ),
    )
    val gridList = (_list.map(CallingObject::clone) as ArrayList<CallingObject>).onEach {
        it.type = ViewType.GRID_POSITION
    }

    fun changeGridType(): List<CallingObject> = gridList.onEach {
        if (it.type != ViewType.GRID_POSITION) {
            it.type = ViewType.GRID_POSITION
        }
    }

    fun addItem(model: CallingObject) = _list.add(model)

    fun drawableToUri(@DrawableRes id: Int) =
        Uri.parse("android.resource://" + R::class.java.`package`?.name + "/" + id)

}