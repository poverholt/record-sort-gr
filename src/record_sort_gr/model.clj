(ns record-sort-gr.model)

(def records (atom []))

(defn create-rec [rec] (swap! records conj rec))

(defn read-recs [] @records)

(defn reset "For testing only." [] (reset! records []))
