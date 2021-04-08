(ns record-sort-gr.model)

(def records (atom []))

(defn create-rec [lname fname gender color bdate]
  (swap! records conj {:lname lname :fname fname :gender gender :color color :bdate bdate}))

(defn read-recs [] @records)

;; (def date-format (java.text.SimpleDateFormat. "MM/dd/yyyy"))
;;(.parse date-format "3/4/2001")         

;; (create-rec "Ov" "Pete" "M" "blue" (.parse date-format "3/4/2001"))
;; (create-rec "Butt" "Bee" "F" "white" (.parse date-format "3/2/2001"))


;; (def recs (record-sort-gr.sort/gender (read-recs)))

;; (map #(update % :bdate record-sort-gr.bdate/bdate->str) recs)
