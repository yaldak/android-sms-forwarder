package cc.kako.smsgateway.api.util.intent;

import android.Manifest;
import android.app.Activity;

import androidx.core.app.ActivityCompat;

import java.util.Objects;

public class IncomingCallActionUtil {
    private IncomingCallActionUtil() {
    }

    public static boolean hasPermissions(final Activity activity) {
        Objects.requireNonNull(activity);

        return ContextUtil.hasPermissions(activity, new String[] {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CALL_LOG
        });
    }

    public static void requestPermission(final Activity activity, final int requestCode) {
        Objects.requireNonNull(activity);

        ActivityCompat.requestPermissions(activity, new String[] {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CALL_LOG
        }, requestCode);
    }
}
