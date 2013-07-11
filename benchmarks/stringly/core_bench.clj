(ns stringly.core-bench
  (:require [perforate.core :refer [defgoal defcase]]
            [stringly.core :refer :all]))

(defgoal bench-reverse-string "reverse-string")
(defcase bench-reverse-string :default
  [] (reverse-string "ABABABAABABAABABABABABABAABABABABABABAAB"))

(defgoal bench-reverse-words "reverse-words")
(defcase bench-reverse-words :default
  [] (reverse-words "ABC DEF XYZ 123"))

(defgoal bench-disenvowel "disenvowel")
(defcase bench-disenvowel :default
  [] (disenvowel "ABCDEFGHIJKLMNOPQRSTUVWXYZ"))

(defgoal bench-rot13 "rot13")
(defcase bench-rot13 :default
  [] (rot13 "The quick brown fox jumped over the lazy dog!!!!"))

(defgoal bench-letter-frequencies "letter-frequencies")
(defcase bench-letter-frequencies :default
  [] (letter-frequencies "AAABBBCCCdddeeeFFF"))

(defgoal bench-palindrome? "palindrome?")
(defcase bench-palindrome? :true-case
  [] (palindrome? "AAAAAAABAAAAAAA"))
(defcase bench-palindrome? :false-case
  [] (palindrome? "AAABBBCCCDDDEEE"))

(defgoal bench-substring? "substring?")
(defcase bench-substring? :true-case
  [] (substring? "AAA" "BAAAB"))
(defcase bench-substring? :false-case
  [] (substring? "AAA" "BAZAB"))

(defgoal bench-longest-repeated-string "longest-repeated-string")
(defcase bench-longest-repeated-string :default
  [] (longest-repeated-string "AABBCCDDDDDDDDEEFF"))

(defgoal bench-decimal-string? "decimal-string?")
(defcase bench-decimal-string? :true-case
  [] (decimal-string? "-1.000005"))
(defcase bench-decimal-string? :false-case
  [] (decimal-string? "1.000005-"))

(defgoal bench-longest-common-substrings "longest-common-substrings")
(defcase bench-longest-common-substrings :no-matches
  [] (longest-common-substrings "AAAAAAA" "CCCCCCC"))
(defcase bench-longest-common-substrings :one-match
  [] (longest-common-substrings "AACCCAA" "BBCCCBB"))
(defcase bench-longest-common-substrings :multiple-matches
  [] (longest-common-substrings "AABBCCDD" "AAZZXXDD"))
(defcase bench-longest-common-substrings :one-match-longest-at-end
  [] (longest-common-substrings "AABBCCDDEEFFFFFFFF"
                                "ZZBBYYDDXXFFFFFFFF"))
(defcase bench-longest-common-substrings :one-match-longest-at-end-java-close-to-the-metal
  [] (binding [*close-to-the-metal* true] 
       (longest-common-substrings "AABBCCDDEEFFFFFFFF"
                                  "ZZBBYYDDXXFFFFFFFF")))
