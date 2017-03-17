package com.github.kvnxiao.kommandant.utility;

/**
 * As of Kotlin 1.10, there is still some performance overhead in splitting strings by a literal.
 * As such, string splitting in Java is currently still more performant than in Kotlin.
 */
public class StringSplitter {

    public static String[] split(String str, String regex, int limit) {
        return str.split(regex, limit);
    }

    public static String[] split(String str, String regex) {
        return str.split(regex);
    }

}
