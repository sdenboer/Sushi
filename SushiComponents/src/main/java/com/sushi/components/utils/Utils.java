package com.sushi.components.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
}
