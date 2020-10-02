package cc.kako.smsgateway.api.dto;

import android.content.Intent;
import android.telephony.SmsMessage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cc.kako.smsgateway.api.util.intent.SmsReceivedActionUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
public class SmsMessageSnippet {
    @Getter
    private long timestamp;

    @Getter
    private String sender;

    @Getter
    private String body;

    public static List<SmsMessageSnippet> fromSmsReceivedAction(final Intent intent) {
        Objects.requireNonNull(intent);

        return SmsReceivedActionUtil.extractSmsMessages(intent).stream()
                .collect(Collectors.groupingBy(SmsMessage::getDisplayOriginatingAddress))
                .entrySet().stream()
                .map(it -> SmsMessageSnippet.builder()
                        .body(it.getValue().stream()
                                .map(SmsMessage::getDisplayMessageBody)
                                .collect(Collectors.joining()))
                        .sender(it.getKey())
                        .timestamp(it.getValue().parallelStream()
                                .mapToLong(SmsMessage::getTimestampMillis)
                                .max()
                                .orElse(0))
                        .build())
                .collect(Collectors.toList());
    }
}
