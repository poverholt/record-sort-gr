(ns record-sort-gr.core-test
  (:require [record-sort-gr.core :as core])
  (:use clojure.test)
  (:import [java.awt Color]
           [java.util Date]))

;;;; Questions...
;;;; * Can I assume it is one record per line. It is implied by examples, but never specified.
;;;; * Should last name characters be restricted at all? Must at least support ' and - for names like O'Conner and
;;;;   Smith-Jones. Should first letter capitals be enforced? This would be required after - in Smith-Jones as well.
;;;; * Should first name characers be restricted at all? Should first letter capital be enforced?
;;;; * How many color names should be supported?
;;;; * Support data that is too large for memory?
;;;;
;;;; Temporary Notes...
;;;; * Use a single internal record format
;;;; * Dates are internally java.util.Date objects for simpler sorting. 
;;;;    * Display format is specified in assignment as M/D/YYYY.
;;;;    * In inputs, it is M/D/YYYY. M and D will be 1 or 2 digits.
;;;;    * Which may mean we cannot directly print the record, but will need some print formatting code.
;;;; * Internal gender is always M or F. In inputs, it can be any portion of male or female and not case sensitive.
;;;; * Favorite color is just a string. It is never sorted or operated on.
;;;; * For huge data that does not fit in memory, sorting would be done by seeking into file and inserting or database.
;;;; * Removes identical redundant records, but for those that conflict, both will exist and be sorted.
;;;;
;;;; Other Tests...
;;;; * Huge datasets
;;;; * Single record in file
;;;; * No <CR> at end of last record

(def empty-check [{}])

(def sorted-check [{:lname "Bailey-Rae" :fname "Corinne" :gender "F", :color Color/BLACK :bdate (.getTime (Date. 1919 2 26))}
                   {:lname "DeGraw" :fname "Gavin" :gender "F" :color Color/BLUE :bdate (.getTime (Date. 1927 2 4))}
                   {:lname "McLachlan" :fname "Sarah" :gender "F" :color Color/CYAN :bdate (.getTime (Date. 1938 1 28))}
                   {:lname "Metheny" :fname "Pat" :gender "M", :color Color/GREEN :bdate (.getTime (Date. 1944 8 12))}
                   {:lname "Plant" :fname "Robert" :gender "M" :color Color/ORANGE :bdate (.getTime (Date. 1958 8 20))}
                   {:lname "Puth" :fname "Charlie" :gender "M" :color Color/RED :bdate (.getTime (Date. 1961 12 2))}])

(def gender-random-check [{:lname "Brodie" :fname "TJ" :gender "F" :color Color/WHITE :bdate (.getTime (Date. 1990 6 7))}
                          {:lname "Anderson" :fname "Frederick" :gender "M" :color Color/YELLOW :bdate (.getTime (Date. 1989 10 2))}
                          {:lname "Nylander" :fname "William" :gender "M" :color Color/RED :bdate (.getTime (Date. 1996 5 1))}
                          {:lname "Tavares" :fname "John" :gender "M" :color Color/BLUE :bdate (.getTime (Date. 1990 9 20))}
                          {:lname "Thornton" :fname "Joe" :gender "M" :color Color/BLUE :bdate (.getTime (Date. 1979 7 2))}])

(def bdate-random-check [{:lname "Thornton" :fname "Joe" :gender "M" :color Color/BLUE :bdate (.getTime (Date. 1979 7 2))}
                         {:lname "Anderson" :fname "Frederick" :gender "M" :color Color/YELLOW :bdate (.getTime (Date. 1989 10 2))}
                         {:lname "Brodie" :fname "TJ" :gender "F" :color Color/WHITE :bdate (.getTime (Date. 1990 6 7))}
                         {:lname "Tavares" :fname "John" :gender "M" :color Color/BLUE :bdate (.getTime (Date. 1990 9 20))}
                         {:lname "Nylander" :fname "William" :gender "M" :color Color/RED :bdate (.getTime (Date. 1996 5 1))}])

(def lname-random-check [{:lname "Anderson" :fname "Frederick" :gender "M" :color Color/YELLOW :bdate (.getTime (Date. 1989 10 2))}
                         {:lname "Brodie" :fname "TJ" :gender "F" :color Color/WHITE :bdate (.getTime (Date. 1990 6 7))}
                         {:lname "Nylander" :fname "William" :gender "M" :color Color/RED :bdate (.getTime (Date. 1996 5 1))}
                         {:lname "Tavares" :fname "John" :gender "M" :color Color/BLUE :bdate (.getTime (Date. 1990 9 20))}
                         {:lname "Thornton" :fname "Joe" :gender "M" :color Color/BLUE :bdate (.getTime (Date. 1979 7 2))}])

;; (deftest sort-gender-test
;;   (testing "sort-gender"
;;     (is (= (core/parse "empty-check.txt") (core/sort-gender (core/parse "empty.txt" "empty.txt" "empty.txt"))))
;;     (is (= (core/parse "gender-check") (core/sort-gender (core/parse "pipe-presorted.txt" "comma-presorted.txt" "space-presorted.txt"))))
;;     (is (= (core/parse "gender-check") (core/sort-gender (core/parse "pipe-reversed.txt" "comma-reversed.txt" "space-reversed.txt"))))
;;     (is (= (core/parse "gender-check") (core/sort-gender (core/parse "pipe-shuffled.txt" "comma-shuffled.txt" "space-random.txt"))))))

;; (deftest sort-bdate-test
;;   (testing "sort-bdate"
;;     (is (= (core/parse "empty-check.txt") (core/sort-bdate (core/parse "empty.txt" "empty.txt" "empty.txt"))))
;;     (is (= (core/parse "bdate-check") (core/sort-bdate (core/parse "pipe-presorted.txt" "comma-presorted.txt" "space-presorted.txt"))))
;;     (is (= (core/parse "bdate-check") (core/sort-bdate (core/parse "pipe-reversed.txt" "comma-reversed.txt" "space-reversed.txt"))))
;;     (is (= (core/parse "bdate-check") (core/sort-bdate (core/parse "pipe-shuffled.txt" "comma-shuffled.txt" "space-shuffled.txt"))))))

;; (deftest sort-lname-test
;;   (testing "sort-lname"
;;     (is (= (core/parse "empty-check.txt") (core/sort-lname (core/parse "empty.txt" "empty.txt" "empty.txt"))))
;;     (is (= (core/parse "lname-check") (core/sort-lname (core/parse "pipe-presorted.txt" "comma-presorted.txt" "space-presorted.txt"))))
;;     (is (= (core/parse "lname-check") (core/sort-lname (core/parse "pipe-reversed.txt" "comma-reversed.txt" "space-reversed.txt"))))
;;     (is (= (core/parse "lname-check") (core/sort-lname (core/parse "pipe-shuffled.txt" "comma-shuffled.txt" "space-shuffled.txt"))))))

(deftest sort-empty-test
  (let [parsed-empty (core/parse "fs/empty.txt" "fs/empty.txt" "fs/empty.txt")]
    (testing "empty data"
      (is (= empty-check (core/sort-gender parsed-empty)))
      (is (= empty-check (core/sort-bdate parsed-empty)))
      (is (= empty-check (core/sort-lname parsed-empty))))))

(deftest sort-presorted-test
  (let [parsed-presorted (core/parse "fs/pipe-presorted.txt" "fs/comma-presorted.txt" "fs/space-presorted.txt")]
    (testing "presorted data"
      (is (= sorted-check (core/sort-gender parsed-presorted)))
      (is (= sorted-check (core/sort-bdate parsed-presorted)))
      (is (= sorted-check (core/sort-lname parsed-presorted))))))

(deftest sort-reversed-test
  (let [parsed-reversed (core/parse "fs/pipe-reversed.txt" "fs/comma-reversed.txt" "fs/space-reversed.txt")]
    (testing "reversed data"
      (is (= sorted-check (core/sort-gender parsed-reversed)))
      (is (= sorted-check (core/sort-bdate parsed-reversed)))
      (is (= sorted-check (core/sort-lname parsed-reversed))))))

(deftest sort-shuffled-test
  (let [parsed-shuffled (core/parse "fs/pipe-shuffled.txt" "fs/comma-shuffled.txt" "fs/space-shuffled.txt")]
    (testing "shuffled data"
      (is (= sorted-check (core/sort-gender parsed-shuffled)))
      (is (= sorted-check (core/sort-bdate parsed-shuffled)))
      (is (= sorted-check (core/sort-lname parsed-shuffled))))))

;; In the above tests, the test data was created to have the same order, whether sorted by gender, bdate or lname.
;; This way, several tests can use the same check data.
;; In the below, the data will have different orders, depending if the sort is on gender, bdate or lname.
;; Thus, different check data are used.
(deftest sort-random-test
  (let [parsed-random (core/parse "fs/pipe-random.txt" "fs/comma-random.txt" "fs/space-random.txt")]
    (testing "random data"
      (is (= gender-random-check (core/sort-gender parsed-random)))
      (is (= bdate-random-check (core/sort-bdate parsed-random)))
      (is (= lname-random-check (core/sort-lname parsed-random)))
      )))

(deftest ^:disabled next-test
  (testing "temporary"
    (is (= 1 2))))
    

