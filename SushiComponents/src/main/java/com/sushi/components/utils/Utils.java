package com.sushi.components.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    public static String bytesToFileSize(long byteValue) {
        double value;
        String dataUnit;
        if (byteValue < 10_000) {
            value = byteValue / (1024.0);
            dataUnit = "kb";
        } else {
            value = byteValue / (1024.0 * 1024.0);
            dataUnit = "mb";
        }
        BigDecimal bigDec = BigDecimal.valueOf(value);
        bigDec = bigDec.setScale(2, RoundingMode.HALF_UP);
        return bigDec.toPlainString() + dataUnit;
    }

    public static int getPercentage(long current, long total) {
        return (int) (current * 100.0 / total + 0.5);
    }
}
