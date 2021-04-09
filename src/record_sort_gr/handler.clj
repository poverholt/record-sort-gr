(ns record-sort-gr.handler
  (:require [record-sort-gr.bdate :as bdate]
            [record-sort-gr.model :as mdl]
            [record-sort-gr.parse :as parse]
            [record-sort-gr.sort :as sort]))

(def ok-response {:status 200
                  :headers {"Content-Type" "application/json"}
                  :body {:success true
                         :message "Records were reset."}})

(defn ok-response [body] {:status 200
                          :headers {"Content-Type" "application/json"} ;; TODO: Can we add this in route wrapper instead?
                          :body body})

(defn create-success-response [rec] {:status 201
                                     :headers {"Content-Type" "application-json"}
                                     :body (assoc rec :success true
                                                      :message "Record was created.")})

(defn bad-request-response [body] {:status 400
                                   :headers {"Content-Type" "application/json"} ;; TODO: Can we add this in route wrapper instead?
                                   :body body})


;; Invalid syntax: No 'data' parameter found.
;; Field count mismatch. Expected x fields, but received y fields.
;; Invalid gender format. The following case-insensitive values are valid: f, female, m, male.
;; Invalid date format. Date must be in format mm/dd/yyyy. Single character month and date are allowed.
(defn handle-reset-recs
  "Reset to empty list of records"
  [req]
  (do
    (mdl/reset)
    ;;ok-response ;; TODO: Why does this cause 500: Internal server error about JSON encode object of class HTTPInputOverHTTP???
    {:status 200}
    ;;"<h1>Records reset!</h1>"  ;; This works too.
    ))

(defn handle-create-rec
  "Adds one record. Returns success status."
  [req]
  (let [line (get-in req [:params "data"])
        rec (parse/line->rec line)]
    (if (:error rec)
      (bad-request-response rec)
      (do
        (mdl/create-rec rec)
        (create-success-response rec)))))
    
(defn- handle-recs
  "Returns sorted records as JSON."
  [sort-fn]
  (let [sorted-recs (sort-fn (mdl/read-recs))
        body (map #(update % :bdate bdate/bdate->str) sorted-recs)]
    (ok-response body)))

(defn handle-recs-gender [req] (handle-recs sort/gender))
(defn handle-recs-bdate  [req] (handle-recs sort/bdate))
(defn handle-recs-lname  [req] (handle-recs sort/lname)) 








