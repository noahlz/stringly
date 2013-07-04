(ns stringly.service
  (:require stringly.core))

(def ^:private stringly-fns
  (let [metadata (->> (ns-publics 'stringly.core) 
                      vals 
                      (filter #(:api (meta %))) 
                      (map meta))]
    (map (fn [{n :name [args] :arglists}] 
           {:name (str n) :arity (count args)})
         metadata)))

(defn list-operations [] stringly-fns)

(defn invoke [f args]
  {:pre [(string? f) (vector? args) (every? string? args)]}
  (if-let [stringly-fn (ns-resolve 'stringly.core (symbol f))]
    (apply stringly-fn args)
    (throw (IllegalArgumentException. (str "Unknown operation " f)))))
