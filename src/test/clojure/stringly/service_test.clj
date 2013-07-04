(ns stringly.service-test
  (:use stringly.service
        midje.sweet))

(fact "about list-operations"
  (count (list-operations)) => pos?
  (list-operations)
    => (has every? (fn [{:keys [name arity]}] (and name arity))))

(facts "we can call stringly core functions from data"
  (invoke "reverse-string" ["abcd"])
     => "dcba"
  (invoke "letter-frequencies" ["aaabbbbccd"])
     => {\a 3, \b 4, \c 2, \d 1}
  (invoke "palindrome?" ["aaabbaaa"])
     => true
  (invoke "substring?" ["bb" "aaabbaaa"])
     => true
  (invoke "longest-repeated-string" ["abbbbccccdeeeeeef"])
     => "eeeeee"
 (invoke "reverse-words" ["The rain in Spain."])
     => "ehT niar ni niapS."
  (invoke "decimal-string?" ["1.0"])
     => true)
