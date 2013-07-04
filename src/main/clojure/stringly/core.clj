(ns stringly.core
  "String functions as-a-Service"
  (:import stringly.core.Util))

(defn reverse-string [s]
  ;; just using "reverse" would be boring.
  (clojure.string/join (reduce conj '() s)))

(defn reverse-words [s]
  (Util/reverseWords s))

(defn disenvowel [s]
  (apply str (remove #{\a \e \i \o \u 
                       \A \E \I \O \U} s)))

(defn rot13 [s]
  (apply str
    (for [^char c s]
      (let [idx (int c)
            A (int \A) Z (int \Z)
            a (int \a) z (int \z)
            is-ascii-letter (or (and (>= idx A) (<= idx Z)) 
                                (and (>= idx a) (<= idx z)))] 
        (if is-ascii-letter
          (let [offset (if (Character/isUpperCase c) A a)]
            (-> (- idx offset) (+ 13) (mod 26) (+ offset) char))
          c)))))

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

;; TODO...
(defn longest-common-substring [s1 s2]
  ;; See this solution by @cgrande 
  ;; http://stackoverflow.com/a/14958791/7507
  ;; Clojure mailing list discussion: 
  ;; https://groups.google.com/forum/#!topic/clojure/byHO-9t6X4U[1-25-false]
  nil)

(defn longest-repeated-string [s]
  (apply str
    (let [xs (partition-by identity s)]
      (reduce #(if (> (count %1) (count %2)) %1 %2) '() xs))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; decimal-string?

(defn- char-type [index ^Character c]
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

