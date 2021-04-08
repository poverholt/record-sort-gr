(ns record-sort-gr.model)

(def records (atom []))

(defn create-rec [lname fname gender color bdate]
  (swap! records #(conj % {:lname lname :fname fname :gender gender :color color :bdate bdate})))

(defn read-recs [] @records)

