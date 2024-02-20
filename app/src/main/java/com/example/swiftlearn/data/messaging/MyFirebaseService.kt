package com.example.swiftlearn.data.messaging

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.swiftlearn.MainActivity
import com.example.swiftlearn.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

/**
 * Servicio de Firebase Cloud Messaging para manejar la recepción y visualización de notificaciones push.
 */
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseService: FirebaseMessagingService() {
    private val random = Random

    /**
     * Método que se llama cuando se recibe un mensaje de notificación push.
     *
     * @param message Mensaje remoto recibido.
     */
    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let{
            sendNotification(it)
        }
    }

    /**
     * Método para enviar una notificación al dispositivo.
     *
     * @param message Detalles de la notificación recibida.
     */
    private fun sendNotification(message: RemoteMessage.Notification) {
        // Creamos un intent para abrir la actividad principal al hacer click en la notificación
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        }

        // Creamos un PendingIntent para la actividad principal
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, FLAG_IMMUTABLE
        )

        // Obtenemos el ID del canal de notificación
        val channelId = this.getString(R.string.fcm_channel_notifications)
        // Construimos la notificación con los detalles recibidos del mensaje
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(message.title)
            .setContentText(message.body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.icon_google)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        // Obtenemos el servicio de notificaciones
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Creamos el canal de notificación si la versión de Android es mayor o igual a Oreo
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }

        // Mostramos la notificación
        manager.notify(random.nextInt(), notificationBuilder.build())
    }

    /**
     * Nombre del canal de notificaciones para Firebase Cloud Messaging.
     */
    companion object {
        const val CHANNEL_NAME = "FCM notification channel"
    }
}