(ns record-sort-gr.model)

(def records (atom []))

(defn reset [] (reset! records []))

(defn create-rec [rec] (swap! records conj rec))

(defn read-recs [] @records)

