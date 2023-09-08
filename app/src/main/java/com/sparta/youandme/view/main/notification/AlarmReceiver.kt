package com.sparta.youandme.view.main.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sparta.youandme.R
import com.sparta.youandme.data.CallObjectData
import com.sparta.youandme.model.CallingObject
import com.sparta.youandme.view.main.MainActivity
import java.util.*


class AlarmReceiver : BroadcastReceiver() {
    private lateinit var manager: NotificationManager
    private val list = CallObjectData.list.map { it.id }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        createNotificationChannel(context)
        // notification manager
        manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val bundle = intent.getBundleExtra("bundle")
        val model = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle?.getParcelable("obj", CallingObject::class.java)
        } else {
            bundle?.getParcelable("obj")
        }
        // 알람 클릭시 넘어갈 인텐트 준비
        val alarmIntent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 101, alarmIntent, PendingIntent.FLAG_IMMUTABLE)
        val builder = createNotification(context, model, pendingIntent)
        // noti 등록
        // 각자의 noti에도 고유값필요
        val id = list.indexOf(model?.id)
        println(model)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(context).notify(id, builder)
    }

    private fun createNotification(
        context: Context,
        model: CallingObject?,
        pendingIntent: PendingIntent
    ): Notification {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        builder.apply {
            //알림창 제목
            setSmallIcon(R.drawable.notification_icon)
            setContentTitle("연락처 알림")
            setContentText("${model?.name}님에게 전화를 걸 시간입니다.")
            setContentIntent(pendingIntent)
            setChannelId(CHANNEL_ID)
            //알림창 터치시 자동 삭제
            setAutoCancel(true)
            setContentIntent(pendingIntent)
        }

        return builder.build()
    }

    private fun createNotificationChannel(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "this is YOU & ME"
                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                setSound(uri, audioAttributes)
                enableVibration(true)
            }
            channel.description = CHANNEL_DESCRIPTION
            (context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_NAME = "Call Notification"
        private const val CHANNEL_DESCRIPTION = "Call Alarm"
        private const val CHANNEL_ID = "channel id"
    }
}