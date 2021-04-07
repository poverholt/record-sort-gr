(ns record-sort-gr.format
  (:require [record-sort-gr.bdate :as bdate]))

(defn rec->str
  [rec]
  (str (:lname rec) ", " (:fname rec) ", " (:gender rec) ", " (:color rec) ", " (bdate/bdate->str (:bdate rec))))
