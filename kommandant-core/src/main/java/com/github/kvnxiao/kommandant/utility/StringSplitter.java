package com.github.kvnxiao.kommandant.utility;

/**
 * As of Kotlin 1.10, there is still some performance overhead in splitting strings by a literal.
 * As such, string splitting in Java is currently still more performant than in Kotlin.
 */
public class StringSplitter {

    /**
     * Java string split method.
     * @param str The string to split.
     * @param regex The regex pattern in string form.
     * @param limit The maximum number of substrings to split into.
     * @return A string array of the original string split by the provided regex and limit.
     */
    public static String[] split(String str, String regex, int limit) {
        return str.split(regex, limit);
    }

    /**
     * Java string split method with no limit.
     * @param str The string to split.
     * @param regex The regex pattern in string form.
     * @return A string array of the original string split by the provided regex.
     */
    public static String[] split(String str, String regex) {
        return str.split(regex);
    }

    /**
     * Single space character in String form: " ".
     */
    public static final String SPACE_LITERAL = " ";

    /**
     * Single space character: ' '.
     */
    public static final char SPACE_CHAR = ' ';

}
