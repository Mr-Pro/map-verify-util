package cn.lger.utils;

import java.util.Collection;
import java.util.Collections;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-03-30.
 */
public class Util {

    public static String[] stringArrConcat(String[] first, String[] second) {
        String[] temp = new String[first.length + second.length];
        System.arraycopy(first, 0, temp, 0, first.length);
        System.arraycopy(second, 0, temp, first.length, second.length);
        return temp;
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(String[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isBlank(String s) {
        return s == null || s.length() == 0 || s.trim().length() == 0;
    }
}
