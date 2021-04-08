(ns record-sort-gr.handler
  (:require [cheshire.core :as json]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [record-sort-gr.bdate :as bdate]
            [record-sort-gr.gender :as gdr]
            [record-sort-gr.model :as mdl]
            [record-sort-gr.sort :as sort]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :refer [response file-response resource-response]]))

(defn greet
  "Handler using ring.util.response."
  [req]
  (response "Hello clojure world")) ;; The text gets delivered in a file that browser offers to put in downloads folder

(greet {})

;; (def redirect-response {:status 302
;;                         :headers {"Location" "/records"}
;;                         :body ""})

(def success-response {:status 201
                       :headers {"Content-Type" "application-json"}
                       :body {:success true}})

;;(def success-response-2 {:status 200
;;                         :headers {"Content-Type" "application-json"}
;;                         :body {:errors nil}})

(defn handle-create-rec
  "Adds one record. Returns success status."
  [req]
  (let [lname (get-in req [:params "lname"]) ;; TODO: Should be a short-cut to get all 5 fields in one sweep!
        fname (get-in req [:params "fname"])
        gender (gdr/str->gender (get-in req [:params "gender"]))
        color (get-in req [:params "color"])
        bdate (bdate/str->bdate (get-in req [:params "bdate"]))
        _ (mdl/create-rec lname fname gender color bdate)]
    ;;redirect-response  ;; TODO: This only makes sense in a view or web page server. Not here!
    success-response
    ))
    
(defn- handle-recs
  "Returns sorted records as JSON."
  [sort-fn]
  {:status 200
   :headers {"Content-Type" "application/json"} ;; TODO: Can we add this in route wrapper too?
   :body (-> (mdl/read-recs)
             (sort-fn)
             ;;json/generate-string ;; TODO: This is done in route wrapper
             )})

(defn handle-recs-gender [req] (handle-recs sort/gender))
(defn handle-recs-bdate  [req] (handle-recs sort/bdate))
(defn handle-recs-lname  [req] (handle-recs sort/lname)) 








