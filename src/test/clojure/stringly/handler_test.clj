(ns stringly.handler-test
  (:require [stringly.handler :as handler :only [app]])
  (:use midje.sweet
        ring.mock.request))

;; Note: I have to (slurp) the :body below because ring.middleware.format
;; sometimes returns an input stream

(defmacro check-stringly-post [request-body#
                               expected-status#
                               expected-result#]
  `(fact (str "POST " ~request-body#)
     (let [{status# :status response-body# :body}
              (handler/app (-> (request :post "/stringly")
                               (body ~request-body#)
                               (header "Accept" "application/json")
                               (content-type "application/json")))]
       [status# (if (instance? java.io.InputStream response-body#)
                  (slurp response-body#)
                  response-body#)]
       => (just [~expected-status# ~expected-result#]))))

(facts "about the stringly service"
  (fact "stringly GET handles json content / accept"
    (let [response (handler/app (-> (request :get  "/stringly")
                                    (header "Accept" "application/json")
                                    (content-type "application/json")))]
      (:status response) => 200
      (slurp (:body response))  => (has-prefix "{\"result\":")))

  (check-stringly-post "{\"f\":\"reverse-string\",\"args\":[\"aabb\"]}"
                       200 "{\"result\":\"bbaa\"}")

  (check-stringly-post "{\"f\":\"palindrome?\",\"args\":[\"aabb\"]}"
                       200 "{\"result\":false}")

  (check-stringly-post "{\"f\":\"longest-repeated-string\",\"args\":[\"aabbbbaa\"]}"
                       200 "{\"result\":\"bbbb\"}")

  (check-stringly-post "{\"f\":\"substring?\",\"args\":[\"bbb\",\"aabbbbbaa\"]}"
                       200 "{\"result\":true}")

  (check-stringly-post "{\"f\":\"letter-frequencies\",\"args\":[\"aabbbbbc\"]}"
                       200 "{\"result\":{\"a\":2,\"b\":5,\"c\":1}}"))

(facts "error handling returns status 500 and a string body"
  (check-stringly-post "jskluj"      500 string?)
  (check-stringly-post "{\"foo\":1}" 500 string?)
  (check-stringly-post "{\"f\":\"palindrome\"}" 500 string?)
  (check-stringly-post "{\"args\":\"blah\"}"     500 string?)
  (check-stringly-post "{\"f\":\"blah?\",\"args\":\"[bleah]\"}" 500 string?))

(fact "about other routes"
  (handler/app (request :get "/"))
   => (contains {:body "Form will go here."}))

(fact "about invalid routes"
  (let [response (handler/app (request :get "/invalid"))]
    (:status response) => 404))
