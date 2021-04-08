(ns record-sort-gr.handler
  (:require [record-sort-gr.bdate :as bdate]
            [record-sort-gr.model :as mdl]
            [record-sort-gr.parse :as parse]
            [record-sort-gr.sort :as sort]))

(def success-response {:status 201
                       :headers {"Content-Type" "application-json"}
                       :body {:success true}})

;;(def success-response-2 {:status 200
;;                         :headers {"Content-Type" "application-json"}
;;                         :body {:errors nil}})

(defn handle-reset-recs
  "Reset to empty list of records"
  [req]
  (do
    (mdl/reset)
    {:status 200}))

(defn handle-create-rec
  "Adds one record. Returns success status."
  [req]
  (let [line (get-in req [:params "data"])
        rec (parse/line->rec line)
        _ (mdl/create-rec rec)]
    success-response))
    
(defn- handle-recs
  "Returns sorted records as JSON."
  [sort-fn]
  (let [sorted-recs (sort-fn (mdl/read-recs))
        body (map #(update % :bdate bdate/bdate->str) sorted-recs)]
    {:status 200
     :headers {"Content-Type" "application/json"} ;; TODO: Can we add this in route wrapper instead?
     :body body}))

(defn handle-recs-gender [req] (handle-recs sort/gender))
(defn handle-recs-bdate  [req] (handle-recs sort/bdate))
(defn handle-recs-lname  [req] (handle-recs sort/lname)) 








