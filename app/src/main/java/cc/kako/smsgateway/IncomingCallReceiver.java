package cc.kako.smsgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Optional;

import cc.kako.smsgateway.api.dto.IncomingCallEventSnippet;

public final class IncomingCallReceiver extends BroadcastReceiver {
    public static final String TAG = "IncomingCallReceiver";

    private final Gson gson = new Gson();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        long receiveTime = System.currentTimeMillis();

        if (context == null || intent == null) {
            return;
        }

        Optional<IncomingCallEventSnippet> snippet
                = IncomingCallEventSnippet.fromIncomingCallAction(intent, receiveTime);

        snippet.ifPresent(s -> {
            final String requestBody = gson.toJson(s);

            Log.i(TAG, requestBody);
        });
    }
}
