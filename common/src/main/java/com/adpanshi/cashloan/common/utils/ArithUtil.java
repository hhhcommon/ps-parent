package com.adpanshi.cashloan.common.utils;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
/**
 * Created by zsw on 2018/6/23 0023.
 */
public class ArithUtil {private static final int DEFAULT_SCALE = 2;
    private static final String FORMAT_TWO = "0.00";

    public static double add(Double value1, Double value2)
    {
        if (value1 == null) {
            return value2 == null ? 0.0D : value2.doubleValue();
        }
        if (value2 == null) {
            return value1.doubleValue();
        }
        BigDecimal b1 = BigDecimal.valueOf(value1.doubleValue());
        BigDecimal b2 = BigDecimal.valueOf(value2.doubleValue());
        return b1.add(b2).doubleValue();
    }

    public static double sub(Double value1, Double value2)
    {
        BigDecimal b1 = BigDecimal.valueOf(value1 == null ? 0.0D : value1.doubleValue());
        BigDecimal b2 = BigDecimal.valueOf(value2 == null ? 0.0D : value2.doubleValue());
        return b1.subtract(b2).doubleValue();
    }

    public static double mul(Double value1, Double value2)
    {
        BigDecimal b1 = BigDecimal.valueOf(value1.doubleValue());
        BigDecimal b2 = BigDecimal.valueOf(value2.doubleValue());
        return b1.multiply(b2).doubleValue();
    }

    public static double div(Double value1, Double value2, int scale)
            throws IllegalAccessException
    {
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = BigDecimal.valueOf(value1.doubleValue());
        BigDecimal b2 = BigDecimal.valueOf(value2.doubleValue());
        return b1.divide(b2, scale, 4).doubleValue();
    }

    public static double scale(Double value1)
    {
        BigDecimal b1 = BigDecimal.valueOf(value1.doubleValue());
        return b1.setScale(2, 4).doubleValue();
    }

    public static double floor(Double value1)
    {
        if (value1 == null) {
            return 0.0D;
        }
        BigDecimal b1 = BigDecimal.valueOf(value1.doubleValue());
        return b1.setScale(0, 5).doubleValue();
    }

    public static double scale(Double value1, int scale)
    {
        if (value1 == null) {
            return 0.0D;
        }
        BigDecimal b1 = BigDecimal.valueOf(value1.doubleValue());
        return b1.setScale(scale, 4).doubleValue();
    }

    public static String formatAmount(Double doubleVal, int scale, String unit)
    {
        if (doubleVal == null) {
            return unit;
        }
        StringBuilder stringBuilder = new StringBuilder("#.");
        for (int i = 1; i <= scale; i++) {
            stringBuilder.append("0");
        }

        DecimalFormat decimalFormat = new DecimalFormat(stringBuilder.toString());
        NumberFormat numberFormat = NumberFormat.getInstance();
        return new StringBuilder().append(numberFormat.format(new Double(decimalFormat.format(doubleVal)))).append(unit).toString();
    }

    public static int convertYuanToFen(Double amount)
    {
        return BigDecimal.valueOf(amount.doubleValue()).multiply(BigDecimal.valueOf(100L)).intValue();
    }

    public static String scaleAmountNoUnit(Double doubleVal)
    {
        return formatAmount(doubleVal, 2, "");
    }

    public static String doubleMULOneHundredThen2String(Double doubleValue)
    {
        if (null == doubleValue) {
            return null;
        }
        Double double2 = Double.valueOf(scale(Double.valueOf(doubleValue.doubleValue() * 100.0D), 2));
        DecimalFormat df = new DecimalFormat("0.00");
        return new StringBuilder().append(df.format(double2)).append("%").toString();
    }

    public static String doubleToPercentage(Double doubleValue)
    {
        if (null == doubleValue) {
            return null;
        }
        Double dl = Double.valueOf(scale(Double.valueOf(doubleValue.doubleValue() * 100.0D), 2));
        if (0.0D == dl.doubleValue()) {
            return "0.00%";
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return new StringBuilder().append(df.format(dl)).append("%").toString();
    }
}