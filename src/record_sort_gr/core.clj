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
    (assoc-in (hdlr req) [:headers "Server"] "Listronica 9000")))

(def app
  (wrap-server
   (wrap-json-response
    (wrap-params
     routes))))

;;(def app
;;  (wrap-defaults routes site-defaults))

(defn -main [port]
  (jetty/run-jetty app                 {:port (Integer. port)}))

(defn -dev-main [port]
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))

(defn -main-script
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



