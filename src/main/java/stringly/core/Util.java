package stringly.core;

import java.util.*;

/**
 * Drop down into Java to get close to the metal.
 */
public final class Util {

    /**
     * Reverse words (alpha and digits) in the sentence.
     */
    public static String reverseWords(String s) {

        char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if(Character.isLetterOrDigit(c)) {
                int startIdx = i;
                int endIdx = findEndIndex(chars, startIdx);
                reverseWordIn(chars, startIdx, endIdx);
                i = endIdx;
            }
        }

        return new String(chars);
    }

    private static int findEndIndex(char[] sentence, final int startIdx) {
        int i = startIdx;
        while(i < sentence.length) {
            if(Character.isLetterOrDigit(sentence[i])) {
                i++;
            } else {
                break;
            }
        }
        return i - 1;
    }

    private static void reverseWordIn(char[] sentence,
                                      final int wordStartIdx,
                                      final int wordEndIdx) {

        // offset len
        int len = wordStartIdx + ((wordEndIdx - wordStartIdx + 1) / 2);

        for (int i = wordStartIdx, j = wordEndIdx; i < len; i++, j--) {
            char tmp = sentence[j];
            sentence[j] = sentence[i];
            sentence[i] = tmp;
        }
    }

    public static void main(String[] args) {
        if(args.length == 0) {
            System.out.println("argument required");
            return;
        }
        System.out.println("input:  " + args[0]);
        String result = reverseWords(args[0]);
        System.out.println("output: " + result);
    }

    /**
     * Java LCS implementation for benchmarking vs Clojure.
     */
    public static Collection<String> longestCommonsSubstring(String s1, String s2) {

        int[][] table = new int[s1.length()][s2.length()];
        int maxlen = 0;
        Set<String> results = new HashSet<>(); 

        for (int i = 0; i < s1.length(); i++) {
            for (int j = 0; j < s2.length(); j++)  {
                if (s1.charAt(i) == s2.charAt(j)) {

                    int currlongest;
                    if (i == 0 || j == 0) {
                        currlongest = 1;
                    } else {
                        currlongest = 1 + table[i - 1][j - 1]; 
                    }
                    table[i][j] = currlongest;
                    
                    if (currlongest > maxlen) {
                       maxlen = currlongest;
                       results = new HashSet<>();
                    }

                    if (currlongest >= maxlen) {
                       String substr = s1.substring(1 + i - maxlen, i + 1);
                       results.add(substr);
                    }
                }
            }
        }

        return results;
    }
}

