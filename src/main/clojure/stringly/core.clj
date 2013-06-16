(ns stringly.core
  "String functions as-a-Service"
  (:import stringly.core.Util))

(defn reverse-string [s]
  ;; just using "reverse" would be boring.
  (clojure.string/join (reduce conj '() s)))

(defn reverse-words [s]
  (Util/reverseWords s))

(defn disenvowel [s]
  (apply str (remove #{\a \e \i \o \u} s)))

(defn letter-frequencies [s]
  (frequencies (seq s)))

(defn palindrome? [s]
  (if-let [s (seq s)]
    (= s (reverse s))))

(defn substring? [sub s]
  (if (> (count sub) (count s))
    false
    (let [sub (seq sub)
          candidates (partition (count sub) 1 s)]
      (not (nil? (first (filter #(= sub %) candidates)))))))

(defn longest-repeated-string [s]
 (apply str
   (let [xs (partition-by identity s)]
     (reduce #(if (> (count %1) (count %2)) %1 %2) '() xs))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; decimal-string?

(defn- char-type [index c]
  (cond
    (Character/isDigit c)        :digit
    (and (= \- c) (zero? index)) :negative-sign
    (= \. c)                     :decimal-point))

(defn- update-reduction [m index c]
  (if-let [k (char-type index c)]
    (update-in m [k] inc)
    (reduced false)))

(defn- reduce-decimal-string-fn [m [index c]]
  (let [updated-m (update-reduction m index c)
        {decimal-points :decimal-point} updated-m]
    (if (#{0 1} decimal-points)
      updated-m
      (reduced false))))

(defn decimal-string?
  "Test if a given string is a decimal, i.e. \"-1.003\"
   Does not support commas or leading/trailing whitespace.
   Allows zero-padding on either side."
  [s]
  {:post [(instance? Boolean %)]}
  (let [analysis (reduce reduce-decimal-string-fn
                         {:digit 0, :decimal-point 0, :negative-sign 0}
                         (map-indexed vector s))]
    (if (map? analysis)
      (pos? (:digit analysis))
      analysis)))

