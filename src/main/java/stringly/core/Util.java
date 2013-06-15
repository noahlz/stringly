package stringly.core;

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
}

