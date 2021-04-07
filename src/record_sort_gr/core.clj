(ns record-sort-gr.core
  (:gen-class)
  (:require [record-sort-gr.format :as format]
            [record-sort-gr.parse :as parse]
            [record-sort-gr.sort :as sort]))

(defn -main
  "Parses the original shuffled test data files, then prints in three orders.
  1. gender-lname ascending, 2. bdate ascending, 3. lname descending."
  [& args]
  (let [fnames ["test/record_sort_gr/fs/pipe-shuffled.txt"
                "test/record_sort_gr/fs/comma-shuffled.txt"
                "test/record_sort_gr/fs/space-shuffled.txt"]
        recs (parse/files->recs fnames)
        gender-sorted-lines (map format/rec->str (sort/gender recs))
        bdate-sorted-lines (map format/rec->str (sort/bdate recs))
        lname-sorted-lines (map format/rec->str (sort/lname recs))]
    (doseq [line (concat ["" "Sorted by Gender" "----------------"]
                         gender-sorted-lines
                         ["" "Sorted by Birthdate" "-------------------"]
                         bdate-sorted-lines
                         ["" "Sorted by Lastname Descending" "-----------------------------"]
                         lname-sorted-lines)]
      (println line))))


(parse/file->recs "test/record_sort_gr/fs/pipe-shuffled.txt")
