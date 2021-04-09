(ns record-sort-gr.bdate
  (:require [clojure.string :as str])
  (:import [java.text SimpleDateFormat]
           [java.util Date]))

(def date-format (SimpleDateFormat. "MM/dd/yyyy"))
(.setLenient date-format false)

(def error "DATE-ERROR")

(defn str->bdate
  [s]
  (try
    (.parse date-format s)
    (catch Exception e error)))

(defn- strip-leading-date-0s
  [s]
  (-> s
      (str/replace #"^0" "")
      (str/replace #"/0" "/")))

(defn bdate->str [date] (strip-leading-date-0s (.format date-format date)))
