(defproject stringly "0.1.0-SNAPSHOT"
  :description "String Functions as-a-Service"
  :url "http://stringly.heroku.com/"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring "1.2.0-beta3"]
                 [ring-middleware-format "0.3.0"]
                 [compojure "1.1.5"]]

  :source-paths      ["src/main/clojure"]
  :test-paths        ["src/test/clojure"]
  :java-source-paths ["src/main/java"]
  :resource-paths    ["src/main/resources"]

  :jvm-opts ["-server"]

  :injections [(require 'clojure.pprint)]

  :plugins [[lein-ring "0.8.5" :exclusions [org.clojure/clojure]]]
  :ring { :handler stringly.handler/app
          :reload-paths ["src/main/clojure"
                         "src/main/resources"] }

  :profiles {:test {:dependencies [[midje "1.5.1"]
                                   [ring-mock "0.1.4"]]
                    :plugins [[lein-midje "3.0.1"]]}
             :dev [:test {:repl-options
                           {:init (do (use 'midje.repl) (autotest))}}]
             :perf {:plugins [[perforate "0.3.2"]]}}

  :aliases {"midje" ["with-profile" "test" "midje"]
            "test" ["with-profile" "test" "midje"]
            "perf" ["with-profile" "perf" "perforate"]})
