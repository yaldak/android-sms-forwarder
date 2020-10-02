package cc.kako.smsgateway.api.dto;

import android.content.Intent;
import android.telephony.TelephonyManager;

import java.util.Objects;
import java.util.Optional;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
public class IncomingCallEventSnippet {
    @Getter
    private long timestamp;

    @Getter
    private String state;

    @Getter
    private String number;

    public static Optional<IncomingCallEventSnippet> fromIncomingCallAction(final Intent intent,
                                                                            final long timestamp) {
        Objects.requireNonNull(intent);

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

        if (state == null || number == null
                || !TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(intent.getAction())) {
            return Optional.empty();
        }

        return Optional.of(IncomingCallEventSnippet.builder()
                .number(number)
                .state(state)
                .timestamp(timestamp)
                .build());
    }
}
