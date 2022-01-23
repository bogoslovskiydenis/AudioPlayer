package com.example.audioplayer.utils

interface FormatTimeUi {
    fun format(data: Int): String

    class Base : FormatTimeUi {
        override fun format(data: Int): String {
            val seconds: String = (data % 60).toString()
            val minutes: String = (data / 60).toString()
            val totalOut = "$minutes:$seconds"
            val totalNew = "$minutes:0$seconds"
            return if (seconds.length == 1) totalNew else totalOut
        }
    }
}