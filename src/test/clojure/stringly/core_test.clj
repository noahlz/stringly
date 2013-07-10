(ns stringly.core-test
  (:use stringly.core
        midje.sweet))

(facts "about the core stringly functions"
  (reverse-string "abcd") => "dcba"
  (reverse-words "The rain in Spain.") => "ehT niar ni niapS."
  (disenvowel "abcde") => "bcd"
  (disenvowel "AbcdEoOUiIy") => "bcdy"
  (letter-frequencies "aaabbbbccd") => {\a 3, \b 4, \c 2, \d 1}
  (letter-frequencies "aa bb 11 22 CC ,,") => {\a 2 \b 2 \C 2}
  (palindrome? "aabaa") => true
  (palindrome? "aabac") => false
  (substring? "aa" "bbaacc") => true
  (substring? "aa" "bbabcc") => false
  (longest-repeated-string "abbbbccccdeeeeeef") => "eeeeee"
  (decimal-string? "1")   => true
  (decimal-string? "a")   => false
  (decimal-string? "-1")  => true
  (decimal-string? "1.0") => true
  (decimal-string? ".01") => true
  (decimal-string? "-.")  => false
  (decimal-string? "-0-") => false
  (decimal-string? "--0") => false
  (decimal-string? "00.00") => true
  (decimal-string? "1..0") => false
  (rot13 "The Quick Brown Fox Jumps Over The Lazy Dog!") 
      => "Gur Dhvpx Oebja Sbk Whzcf Bire Gur Ynml Qbt!"
  (longest-common-substrings "AABC" "ABCA") => ["ABC"]
  (longest-common-substrings "ABABC" "BABCA") => ["BABC"]
  (longest-common-substrings "aaaAABC" "ABCaaaA") => ["aaaA"]
  (longest-common-substrings "C" "ZZZZzzzZZZCA") => ["C"]
  (longest-common-substrings "ZZZZzzzZZZCA" "C") => ["C"]
  (longest-common-substrings "" "A") => empty?
  (longest-common-substrings "A" "") => empty?
  (longest-common-substrings "AAATABBD" "AAAZABBC") => (contains  #{"AAA" "ABB"})
  (longest-common-substrings "AABBAACC" "ZAAZBBZCC") => (contains  #{"AA" "BB" "CC"})
  (longest-common-substrings "AABBCCDDEEFFFFFFFF"
                             "ZZBBYYDDXXFFFFFFFF") => ["FFFFFFFF"]
  (longest-common-substrings "AABBCCDDEEFFFFFFFF"
                             "FFFFFFFFZZBBYYDDXX") => ["FFFFFFFF"])
