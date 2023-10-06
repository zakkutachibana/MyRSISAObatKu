package com.zak.rsisaobatku.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.zak.rsisaobatku.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ReminderReceiver : BroadcastReceiver() {

    //Ini nerima broadcast, nanti baru jalanin show notification
    //ASUMSI: ID nanti dimasukin disini, lewat mana tapi ;_;
    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val title =
            if (type.equals(TYPE_MORNING, ignoreCase = true)) TYPE_MORNING
            else if (type.equals(TYPE_NOON, ignoreCase = true)) TYPE_NOON
            else TYPE_NIGHT
        val notifId =
            if (type.equals(TYPE_MORNING, ignoreCase = true)) ID_MORNING
        else if (type.equals(TYPE_NOON, ignoreCase = true)) ID_NOON
        else ID_NIGHT
        if (message != null) {
            showAlarmNotification(context, title, message, notifId)
        }
    }

    private fun showAlarmNotification(
        context: Context,
        title: String,
        message: String,
        notifId: Int
    ) {
        val channelId = "Channel_1"
        val channelName = "ObatKu Channel"
        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_time)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    //ini yang dipanggil di add update activity
    //ini yang nge trigger bagian onReceive
    //EXTRA ID MASUKIN SINI BRO
    fun setRepeatingAlarm(context: Context, type: String, time: String, message: String, id: Int) {
        if (isDateInvalid(time, TIME_FORMAT)) return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)
        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }

    companion object {
        const val TYPE_MORNING = "Pengingat Pertama"
        const val TYPE_NOON = "Pengingat Kedua"
        const val TYPE_NIGHT = "Pengingat Ketiga"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"

        const val ID_MORNING = 100
        const val ID_NOON = 101
        const val ID_NIGHT = 102

        private const val TIME_FORMAT = "HH:mm"
    }
}