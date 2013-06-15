(ns stringly.service
  (:require stringly.core))

(def ^:private stringly-fns
  (let [metadata (->> (ns-publics 'stringly.core) vals (map meta))]
    (map (fn [{n :name [args] :arglists}] ;; only the first arg signature
           {:name n :arity (count args)})
         metadata)))

(def ^:private stringly-fn-names
  (keys (ns-publics 'stringly.core)))

(defn list-operations [] stringly-fns)

;; TODO using function metadata, use truthy to convert truthy to true and nil to false
(defn invoke [f args]
  {:pre [(string? f) (vector? args) (every? string? args)]}
  (if-let [stringly-fn (ns-resolve 'stringly.core (symbol f))]
    (apply stringly-fn args)
    (throw (IllegalArgumentException. (str "Unknown operation " f)))))
