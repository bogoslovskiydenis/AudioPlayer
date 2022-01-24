package com.example.audioplayer.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.example.audioplayer.PlayerActivity
import com.example.audioplayer.R
import com.example.audioplayer.action.Actions.*
import com.example.audioplayer.aplication.ApplicationClass
import com.example.audioplayer.utils.BytesFromUri

interface NotificationUi {

    fun show(position: Int)

    class Base(
        private val context: Context,
        private val mediaSessionCompat: MediaSessionCompat,
        private val bytesFromUri: BytesFromUri
    ) : NotificationUi {

        private val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        private val previous = Previous()
        private val next = Next()
        private val play = Play()

        override fun show(position: Int) {

            val prevIntent = previous.intent(Intent(context, NotificationReceiver::class.java))
            val prevPending = PendingIntent.getActivity(
                context,
                0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
            val nextIntent = next.intent(Intent(context, NotificationReceiver::class.java))
            val nextPending = PendingIntent.getBroadcast(
                context,
                0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
            val pauseIntent = play.intent(Intent(context, NotificationReceiver::class.java))
            val playPending = PendingIntent.getBroadcast(
                context,
                0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
            val picture: ByteArray? = bytesFromUri.albumArt(PlayerActivity.listSongs[position].path)
            val bitmap: Bitmap? = BitmapFactory.decodeByteArray(picture, 0, picture!!.size)
            var builder: NotificationCompat.Builder =
                NotificationCompat.Builder(context, ApplicationClass.CHANNEL_ID_1)
                    .setSmallIcon(R.drawable.ic_play)
                    .setLargeIcon(bitmap)
                    .setContentTitle(PlayerActivity.listSongs[position].title)
                    .setContentText(PlayerActivity.listSongs[position].artist)

            builder = previous.makeAction(builder, R.drawable.ic_skip_previous, prevPending)
            builder = play.makeAction(builder, R.drawable.ic_play, playPending)
            builder = next.makeAction(builder, R.drawable.ic_skip_next, nextPending)
            val notification = builder
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSessionCompat.sessionToken)
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .build()
            notificationManager.notify(0, notification)
        }
    }
}