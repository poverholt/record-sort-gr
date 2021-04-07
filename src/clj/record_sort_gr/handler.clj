(ns record-sort-gr.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :refer [response file-response resource-response]]))

(defn hello-world
  "Handler using ring.util.response."
  [req]
  (response "Hello clojure world")) ;; The text gets delivered in a file that browser offers to put in downloads folder

(defroutes app-routes
  (GET "/" [] "Hello GR!")
  (GET "/hello" [] hello-world)
  (GET "/index" [] (resource-response "public/index.html"))  ;; Browser saves it to file rather than parsing it as HTML!?
  (GET "/records/gender" [] nil)
  (GET "/records/birthdate" [] nil)
  (GET "/records/name" [] nil)
  (POST "/records" [] nil)
  (route/not-found "<h1>The page requested does not exist.</h1>"))

(def app
  (wrap-defaults app-routes site-defaults))

(hello-world {})

(resource-response "public/index.html")
