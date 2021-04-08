(ns record-sort-gr.bdate
  (:require [clojure.string :as str])
  (:import [java.text SimpleDateFormat]
           [java.util Date GregorianCalendar]))

(def date-format (SimpleDateFormat. "MM/dd/yyyy"))

(def date-error (Date. 0)) ; TODO: This is not a great error marker, because it is a valid date.

(defn str->bdate
  [s]
  (try
    (.parse date-format s)
    (catch Exception e date-error)))

(defn- strip-leading-date-0s
  [s]
  (-> s
      (str/replace #"^0" "")
      (str/replace #"/0" "/")))

(defn bdate->str [date] (strip-leading-date-0s (.format date-format date)))
