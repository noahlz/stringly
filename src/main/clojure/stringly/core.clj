(ns stringly.core
  "String functions as-a-Service"
  (:import stringly.core.Util))

;; functions annotated with ^:api are exported to the web service in service.clj

(defn ^:api reverse-string [s]
  ;; just using "reverse" would be boring.
  (clojure.string/join (reduce conj '() s)))

(defn ^:api reverse-words [s]
  (Util/reverseWords s))

(defn ^:api disenvowel [s]
  (apply str (remove #{\a \e \i \o \u 
                       \A \E \I \O \U} s)))

(defn ^:api rot13 [s]
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

(defn ^:api letter-frequencies [s]
  (let [s (filter #(Character/isLetter %) s)]
    (frequencies s)))

(defn ^:api palindrome? [s]
  (if-let [s (seq s)]
    (= s (reverse s))))

(defn ^:api substring? [sub s]
  (if (> (count sub) (count s))
    false
    (let [sub (seq sub)
          candidates (partition (count sub) 1 s)]
      (not (nil? (first (filter #(= sub %) candidates)))))))

(defn ^:api longest-repeated-string [s]
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

(defn ^:api decimal-string?
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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; longest-common-substring - not idiomatic! 

;; Adapted from this Common Lisp implementation: http://bit.ly/19mgUfY
;; Also see this Clojure mailing list discussion on performance: 
;; https://groups.google.com/forum/#!topic/clojure/byHO-9t6X4U[1-25-false]
(defn ^:api longest-common-substrings [^String s1 ^String s2]
  (let [s1len (count s1)
        s2len (count s2)
        L (make-array Long/TYPE s1len s2len)]
    (loop [i 0 z-and-result [0 #{}]]
      (if (< i s1len)
        (recur (inc i) 
               (loop [j 0 [z result] z-and-result]
                 (if (< j s2len)
                   (recur (inc j)
                          (if (= (.charAt s1 i) (.charAt s2 j))
                            (let [currlongest 
                                   (aset-long L i j (if (or (zero? i) (zero? j))
                                                      1
                                                      (inc (aget L (dec i) (dec j))))) 
                                  longer-than-z? (> currlongest z)
                                  z (if longer-than-z? currlongest z)
                                  result (if longer-than-z? #{} result)
                                  substr (.substring s1 (inc (- i z)) (inc i))]
                              [z (if (>= currlongest z) (conj result substr) result)])
                            [z result]))
                   [z result]))) 
        (vec (fnext z-and-result))))))
