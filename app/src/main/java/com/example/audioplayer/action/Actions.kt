package com.example.audioplayer.action

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat

interface Actions {
    fun compare(actionName: String): Boolean
    fun intent(intent: Intent): Intent
    fun showToast(context: Context)
    fun act(actionPlay: ActionPlay?)
    fun startService(context: Context, intent: Intent)
    fun makeAction(
        builder: NotificationCompat.Builder,
        id: Int, intent: PendingIntent
    ): NotificationCompat.Builder

    abstract class Base : Actions {
        protected abstract val actionName: String
        protected abstract val toastText: String

        override fun compare(actionName: String): Boolean = actionName == actionName
        override fun intent(intent: Intent): Intent = intent.setAction(actionName)
        override fun showToast(context: Context) =
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()

        override fun makeAction(
            builder: NotificationCompat.Builder,
            id: Int,
            intent: PendingIntent
        ) =
            builder.addAction(id, toastText, intent)
    }
    class Previous : Base() {
        override val actionName: String = "actionPrevious"
        override val toastText: String = "Previous"
        override fun act(actionPlay: ActionPlay?) {
            actionPlay?.prevBtnClicked()
        }

        override fun startService(context: Context, intent: Intent) {
            intent.putExtra("ActionName", "actionPlay")
            context.startService(intent)
        }
    }

    class Play : Base() {
        override val actionName: String = "playPause"
        override val toastText: String = "Play"
        override fun act(actionPlay: ActionPlay?) {
           actionPlay?.playPauseBtnClicked()
        }

        override fun startService(context: Context, intent: Intent) {
            intent.putExtra("ActionName", "actionPlay")
            context.startService(intent)
        }
    }

    class Next : Base() {
        override val actionName: String = "actionNext"
        override val toastText: String = "Next"
        override fun act(actionPlay: ActionPlay?) {
            actionPlay?.nextBtnClicked()
        }

        override fun startService(context: Context, intent: Intent) {
            intent.putExtra("ActionName", "actionPlay")
            context.startService(intent)
        }
    }
}