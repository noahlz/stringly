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

;; See https://gist.github.com/noahlz/5943779
(defn ^:api letter-frequencies [s]
  (let [s (filter #(Character/isLetter (char %)) s)]
    (frequencies s)))

(defn ^:api palindrome? [s]
  (if-let [s (seq s)]
    (= s (reverse s))))

(defn ^:api substring? [sub s]
  (if (> (count sub) (count s))
    false
    (let [sub (seq sub)
          candidates (partition (count sub) 1 s)]
      (not (nil? (first (filter #{sub} candidates)))))))

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
(defn- find-lcs-fn [^String s1 ^String s2 ^longs table] ;; <== closure!
  (fn [[z result] i]
    (reduce  
      (fn [[z result] j] 
        (if (= (.charAt s1 i) (.charAt s2 j))
          (let [curr-longest (aset-long table i j 
                               (if (or (zero? i) (zero? j))
                                 1
                                 (inc (aget table (dec i) (dec j))))) 
                longer-than-z? (> curr-longest z)
                z              (if longer-than-z? curr-longest z)
                result         (if longer-than-z? #{} result)]
            [z (if (>= curr-longest z) 
                 (conj result (.substring s1 (inc (- i z)) (inc i))) 
                 result)])
          [z result])) 
      [z result] 
      (range (count s2)))))

(defn ^:api longest-common-substrings 
  "Returns vector of longest common substrings for the two input strings."
  [^String s1 ^String s2]
    (let [table (make-array Long/TYPE (count s1) (count s2))
          result (reduce (find-lcs-fn s1 s2 table) [0 #{}] (range (count s1)))]
      (vec (fnext result))))
