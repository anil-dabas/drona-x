package com.spot.exchange.order.util;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.apache.commons.lang3.StringUtils;

public class TradeUtil {
    public static BigDecimal toBigDecimal(String strValue) {
        if(StringUtils.isEmpty(strValue)){
            return new BigDecimal("0.0");
        }else{
            return new BigDecimal(strValue);
        }
    }

    public static String toStringFromBigDecimal(BigDecimal bdValue) {
        return bdValue.toString();
    }

    public static String getQuoteCurrency(String instId) {
        return instId.split("_")[1];
    }

    public static String getBaseCurrency(String instId) {
        return instId.split("_")[0];
    }

    public static LocalDateTime convertMicrosecondsToLocalDateTime(long microseconds) {
        long seconds = microseconds / 1_000_000;
        long nanos = (microseconds % 1_000_000) * 1_000;

        return LocalDateTime.ofEpochSecond(seconds, (int) nanos, ZoneOffset.UTC);
    }

    public static long convertToMicroseconds(LocalDateTime localDateTime) {
        // Convert LocalDateTime to Instant (UTC)
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);

        // Get seconds and nanoseconds
        long seconds = instant.getEpochSecond();
        long nanoseconds = instant.getNano();

        // Convert to microseconds
        long microseconds = (seconds * 1_000_000) + (nanoseconds / 1_000);
        return microseconds;
    }
}
