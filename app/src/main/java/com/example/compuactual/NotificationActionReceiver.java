package com.example.compuactual;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NotificationActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String modelo = intent.getStringExtra("modelo");
        String numSerie = intent.getStringExtra("numSerie");

        if ("ACEPTAR".equals(action)) {

            sendResponseToServer(context, "ACEPTAR", modelo, numSerie);
        } else if ("RECHAZAR".equals(action)) {

            sendResponseToServer(context, "RECHAZAR", modelo, numSerie);
        }
    }

    private void sendResponseToServer(Context context, String response, String modelo, String numSerie) {

        OkHttpClient client = new OkHttpClient();


        String baseUrl = "https://compuActual:8000";
        String endpoint = "api_send-notification";
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + "/" + endpoint).newBuilder();
        urlBuilder.addQueryParameter("response", response);
        urlBuilder.addQueryParameter("modelo", modelo);
        urlBuilder.addQueryParameter("numSerie", numSerie);
        String url = urlBuilder.build().toString();


        Request request = new Request.Builder()
                .url(url)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Manejar el error en caso de fallo en la solicitud
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // La solicitud fue exitosa, puedes manejar la respuesta del servidor aquí
                    // Por ejemplo, puedes verificar el código de estado y actualizar el estado
                    // de la notificación en tu base de datos local si es necesario
                } else {
                    // La solicitud no fue exitosa, manejar el error si es necesario
                }
            }
        });


        // Opcional: Mostrar un mensaje al usuario
        String message = response.equals("ACEPTAR") ? "Aceptaste la notificación" : "Rechazaste la notificación";
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
