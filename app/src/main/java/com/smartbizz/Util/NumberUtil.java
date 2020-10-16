package com.smartbizz.Util;

import android.text.TextUtils;

public class NumberUtil {
    private static final String TAG = NumberUtil.class.getSimpleName();
    private static final String NULL = "null";

    private NumberUtil() {
    }

    /**
     * Returns the integer value of the given number
     *
     * @param number       The number of generic type
     * @param defaultValue The default integer value to return if the given number is null or empty or is not a valid
     *                     integer number
     * @return The integer value if the number is a valid integer, or default value
     */
    public static <T> int getInt(T number, int defaultValue) {
        if (number != null) {
            if (number instanceof Integer) {
                return (Integer) number;
            } else if (!TextUtils.isEmpty(number.toString()) && !NULL.equalsIgnoreCase(number.toString())) {
                try {
                    return Integer.parseInt(number.toString().replace(",",""));
                } catch (Exception e) {
//                    MyLogger.error(TAG, e);
                }
            }
        }
        return defaultValue;
    }

    /**
     * Returns the integer value of the given number in string format
     *
     * @param number The number of generic type
     * @return The integer value if the number is a valid integer, or 0
     */
    public static <T> int getInt(T number) {
        return getInt(number, 0);
    }

    /**
     * Returns the long value of the given number in string format
     *
     * @param number       The number of generic type
     * @param defaultValue The default long value to return if the given number is null or empty or is not a valid long
     *                     number
     * @return The long value if the number is a valid long, or defaultValue
     */
    public static <T> long getLong(T number, long defaultValue) {
        if (number != null) {
            if (number instanceof Long) {
                return (Long) number;
            } else if (!TextUtils.isEmpty(number.toString())) {
                try {
                    return Long.parseLong(number.toString().replaceAll("\\W", ""));
                } catch (Exception e) {
//                    MyLogger.error(TAG, e);
                }
            }
        }
        return defaultValue;
    }

    /**
     * Returns the long value of the given number
     *
     * @param number The number of generic type
     * @return The long value if the number is a valid long, or 0
     */
    public static <T> long getLong(T number) {
        return getLong(number, 0);
    }

    /**
     * Returns the double value of the given number
     *
     * @param number The number of generic type
     * @return The double value if the number is a valid double, or 0
     */
    public static <T> double getDouble(T number) {
        return getDouble(number, 0.0);
    }

    /**
     * Returns the double value of the given number
     *
     * @param number       The number of generic type
     * @param defaultValue The default double value to return if the given number is null or empty or is not a valid
     *                     double number
     * @return The double value if the number is a valid double, or default value
     */
    public static <T> double getDouble(T number, double defaultValue) {
        if (number != null) {
            if (number instanceof Double) {
                return (Double) number;
            } else if (!TextUtils.isEmpty(number.toString()) && !NULL.equalsIgnoreCase(number.toString())) {
                try {
                    return Double.parseDouble(number.toString());
                } catch (NumberFormatException e) {
//                    MyLogger.error(TAG, e);
                }
            }
        }
        return defaultValue;
    }

    public static double getDecimalFormat(double input) {
        return getDouble(String.format("%.2f", input));
    }

    public static double getDecimalFormat(String input) {
        return getDouble(String.format("%.2f", getDouble(input)));
    }
}
