package com.example.mobilki_iyoyyy;

import android.content.Context;
import io.appwrite.Client;

public class AppwriteClient {
    private static Client client;

    public static Client getClient(Context context) {
        if (client == null) {
            client = new Client(context);

            client = new Client(context, "https://sfo.cloud.appwrite.io/v1");
            client.setProject("68f0d40b00171e5db956");

        }
        return client;
    }
}
