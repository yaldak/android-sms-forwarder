package cc.kako.smsgateway.api.util.intent;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony.Sms.Intents;
import android.telephony.SmsMessage;

import androidx.core.app.ActivityCompat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class SmsReceivedActionUtil {
    private SmsReceivedActionUtil() {
    }

    public static List<SmsMessage> extractSmsMessages(final Intent intent) {
        Objects.requireNonNull(intent);

        Bundle extras = intent.getExtras();

        if (extras == null || !Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            return Collections.emptyList();
        }

        Object formatObject = extras.get("format");
        Object pdusObject = extras.get("pdus");

        if (!(formatObject instanceof String) || !(pdusObject instanceof Object[])) {
            return Collections.emptyList();
        }

        String format = (String) formatObject;

        return Arrays.stream((Object[]) pdusObject)
                .filter(o -> o instanceof byte[])
                .map(pdu -> SmsMessage.createFromPdu((byte[]) pdu, format))
                .collect(Collectors.toList());
    }

    public static boolean hasPermissions(final Activity activity) {
        Objects.requireNonNull(activity);

        return ContextUtil.hasPermissions(activity, Manifest.permission.RECEIVE_SMS);
    }

    public static void requestPermission(final Activity activity, final int requestCode) {
        Objects.requireNonNull(activity);

        ActivityCompat.requestPermissions(activity, new String[] {
                Manifest.permission.RECEIVE_SMS
        }, requestCode);
    }
}
