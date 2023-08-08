package com.    example.compuactual;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Obtener datos de la notificación
        String title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
        String message = remoteMessage.getNotification().getBody();
        String modelo = remoteMessage.getData().get("modelo");
        String numSerie = remoteMessage.getData().get("num_serie");

        // Mostrar notificación al usuario
        showNotification(title, message, modelo, numSerie);
    }

    private void showNotification(String title, String message, String modelo, String numSerie) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.channel_id))

                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Icono predefinido de información
                .setAutoCancel(true);

        // Acción "Aceptar"
        Intent acceptIntent = new Intent(this, NotificationActionReceiver.class);
        acceptIntent.setAction("ACCEPT_ACTION");
        acceptIntent.putExtra("modelo", modelo);
        acceptIntent.putExtra("numSerie", numSerie);
        PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(this, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(android.R.drawable.ic_menu_save, "Aceptar", acceptPendingIntent); // Icono predefinido de checkmark

        // Acción "Rechazar"
        Intent rejectIntent = new Intent(this, NotificationActionReceiver.class);
        rejectIntent.setAction("REJECT_ACTION");
        rejectIntent.putExtra("modelo", modelo);
        rejectIntent.putExtra("numSerie", numSerie);
        PendingIntent rejectPendingIntent = PendingIntent.getBroadcast(this, 0, rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(android.R.drawable.ic_menu_close_clear_cancel, "Rechazar", rejectPendingIntent); // Icono predefinido de X

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        long currentTimeMillis = System.currentTimeMillis();
        int notificationId = (int) currentTimeMillis;

        notificationManager.notify(notificationId, builder.build());
    }
}