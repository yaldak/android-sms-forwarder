package cc.kako.smsgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.List;

import cc.kako.smsgateway.api.dto.SmsMessageSnippet;
import cc.kako.smsgateway.api.util.http.GsonPostRequest;

public final class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";

    private static final String NS_URL = "https://REDACTED.execute-api.us-east-1.amazonaws.com/default/sms-emailer";

    private final Gson gson = new Gson();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (context == null || intent == null) {
            return;
        }

        List<SmsMessageSnippet> snippets = SmsMessageSnippet.fromSmsReceivedAction(intent);

        if (!snippets.isEmpty()) {
            String json = gson.toJson(snippets);

            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(new GsonPostRequest<Void>(NS_URL, json, e -> Log.e(TAG, "Volley error", e)));
        }
    }
}
