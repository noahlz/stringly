(ns stringly.core-test
  (:use stringly.core
        midje.sweet))

(facts "about the core stringly functions"
  (reverse-string "abcd") => "dcba"
  (reverse-words "The rain in Spain.") => "ehT niar ni niapS."
  (disenvowel "abcde") => "bcd"
  (letter-frequencies "aaabbbbccd") => {\a 3, \b 4, \c 2, \d 1}
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
  (decimal-string? "1..0") => false)

(future-facts
  (longest-common-substring "AABC" "ABCA") => "ABC"
  (longest-common-substring "" "A") => nil)
