(ns record-sort-gr.core
  (:gen-class)
  (:require [compojure.core :refer [defroutes ANY GET POST PUT DELETE]]
            [compojure.route :refer [not-found]]
            [record-sort-gr.format :as format]
            [record-sort-gr.handler :as hdlr]
            [record-sort-gr.parse :as parse]
            [record-sort-gr.sort :as sort]
            [ring.adapter.jetty :as jetty]
            [ring.handler.dump :refer [handle-dump]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.reload :refer [wrap-reload]]))

(defroutes routes
  (GET "/" [] "Test only")
  (ANY "/request" [] handle-dump)
  (POST "/records" [] hdlr/handle-create-rec)
  (DELETE "/records" [] hdlr/handle-reset-recs)
  (GET "/records/gender" [] hdlr/handle-recs-gender)
  (GET "/records/birthdate" [] hdlr/handle-recs-bdate)
  (GET "/records/name" [] hdlr/handle-recs-lname)
  (not-found "Route not found."))

(defn wrap-server [hdlr]
  (fn [req]
    (assoc-in (hdlr req) [:headers "Server"] "Record Sort GR")))

(def app
  (wrap-server
   (wrap-json-response
    (wrap-params
     routes))))

(defn step-1-output
  "Parses the original shuffled test data files, then prints in three orders.
  1. gender-lname ascending, 2. bdate ascending, 3. lname descending."
  [& args]
  (let [fnames ["test/record_sort_gr/fs/pipe-shuffled.txt"
                "test/record_sort_gr/fs/comma-shuffled.txt"
                "test/record_sort_gr/fs/space-shuffled.txt"]
        recs (parse/files->recs fnames)
        gender-sorted-lines (map format/rec->str (sort/gender recs))
        bdate-sorted-lines (map format/rec->str (sort/bdate recs))
        lname-sorted-lines (map format/rec->str (sort/lname recs))]
    (doseq [line (concat ["" "Sorted by Gender" "----------------"]
                         gender-sorted-lines
                         ["" "Sorted by Birthdate" "-------------------"]
                         bdate-sorted-lines
                         ["" "Sorted by Lastname Descending" "-----------------------------"]
                         lname-sorted-lines)]
      (println line))))
  
(defn -main [& [port]]
  "If port is -1, a special case, the step 1 output test is run.
   Otherwise, the API Record Sort GR server at either PORT env var, argument port or
   default port 8000, in that order. Env var is defined & used by Heroku."
  (let [port (Integer. (or (System/getenv "PORT") port 8000))]
    (if (= port -1)
      (step-1-output)
      (jetty/run-jetty app {:port port}))))

(defn -dev-main [port]
  "Runs the API Record Sort GR server with debugging features."
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))



