(ns stringly.handler
  (:use [compojure.core :only [defroutes GET POST]]
        [ring.middleware.format :only [wrap-restful-format]])
  (:require [stringly.service :as service]
            [ring.util.response :as response :refer [response status]]
            [compojure.handler :as handler]
            [compojure.route :as route :only [resources not-found]]))

(defn handle-error [err]
  (let [msg (if (instance? Exception err)
              (.getMessage err)
              err)]
    {:status 500 :body msg}))

(defn success [result]
  {:body {:result result}})

(defn wrap-exception [f]
  (fn [request]
    (try
      (f request)
    (catch Exception e
      (handle-error e)))))

(defroutes app-routes
  (POST "/stringly" [f args]
    (if (or (nil? f) (nil? args))
      (handle-error "Parameters 'f' and 'args' required.")
      (try (success (service/invoke f args))
        (catch IllegalArgumentException ex (handle-error ex))
        (catch AssertionError err (handle-error err)))))

  (GET "/stringly" []
    (success (service/list-operations)))

  (GET "/" [] "Form will go here.")

  (route/not-found "Not found.")

  (route/resources "/"))

;; NOTE: see https://github.com/ngrunwald/ring-middleware-format/issues/15
;; json-kw and yaml-kw not enabled by default
(def app
  (-> (wrap-restful-format
       app-routes :formats [:json-kw :edn :yaml-kw :yaml-in-html])
       handler/api
       wrap-exception))
