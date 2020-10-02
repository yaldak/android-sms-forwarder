package cc.kako.smsgateway.api.util.intent;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.Objects;

public final class ContextUtil {
    private ContextUtil() {
    }

    public static boolean hasPermissions(final Context context, final String permission) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(permission);

        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasPermissions(final Context context, final String[] permissions) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(permissions);

        return Arrays.stream(permissions)
                .map(s -> ContextCompat.checkSelfPermission(context, s))
                .allMatch(p -> p == PackageManager.PERMISSION_GRANTED);
    }
}
