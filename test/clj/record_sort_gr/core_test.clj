(ns record-sort-gr.core-test
  (:require [record-sort-gr.core :as core])
  (:use clojure.test)
  (:import [java.awt Color]
           [java.util Date GregorianCalendar]))

;;;; Assumptions...
;;;; * Assume files are one record per line, no multi-line records and no empty lines. However, an empty file is allowed.
;;;; * Last name and first name characters and capitalization are not restricted at all. This allows names that include
;;;;   ' and -, and those that start with lower-case. Examples O'Conner, vanGogh, Zeta-Jones.
;;;;   * Note that van Gogh was changed to vanGogh to match delimiter assumption specified by the assignment. 
;;;; * Sorting is case insensitive. This makes sense for all data, including name directories.
;;;; * Because there are no operations against colors, they are not restricted to web colors or java.awt.Color, and can
;;;;   be any valid string.
;;;; * Gender is any non-empty portion of the string female or male, case-insensitive.
;;;; * Does not support data sizes too larget to fit in memory. Does not use database or advanced sorting using swap to file.
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
;;;; * Large datasets
;;;; * Empty file and files with only white-space gives 0 records
;;;; * Single record in file
;;;; * No <CR> at end of last record
;;;; * Extra fields, too few fields per line
;;;; * Invalid gender format, invalid date format, flexible name formats
;;;; * WS around delimiters and start/end of line
;;;; * Varying original order of combined records in memory
;;;; * identical duplicate record
;;;; * duplicate records and lname/fname/gender are allowed (TODO: Add 2nd Sarah to reversed)


(defn- fp [fname] (str "test/clj/record_sort_gr/fs/" fname))

(defn- date
  "Returns a java Date object given year, month, day as integers. Input month is 1-based, meaning January is month 1."
  [year month day]
  (.getTime (GregorianCalendar. year (dec month) day)))

(def empty-check '())

(def gender-sorted-check
  [{:lname "Bailey-Rae" :fname "Corinne" :gender "F" :color "RED" :bdate (date 1979 2 26)}
   {:lname "McLachlan" :fname "Sarah" :gender "F" :color "Green" :bdate (date 1968 1 28)}
   {:lname "McLachlan" :fname "Sarah" :gender "F" :color "Green" :bdate (date 1968 1 28)}
   {:lname "O'Conner" :fname "Sinead" :gender "F" :color "cyan" :bdate (date 1966 12 8)}
   {:lname "cummings" :fname "e.e." :gender "M" :color "light-gray" :bdate (date 1894 10 14)}
   {:lname "DeGraw" :fname "Gavin" :gender "M" :color "light-blue" :bdate (date 1977 2 4)}
   {:lname "Metheny" :fname "Pat" :gender "M", :color "green" :bdate (date 1954 8 12)}
   {:lname "Plant" :fname "Robert" :gender "M" :color "Blue" :bdate (date 1948 8 20)}
   {:lname "Puth" :fname "Charlie" :gender "M" :color "black" :bdate (date 1991 12 2)}])

(def bdate-sorted-check
  [{:lname "cummings" :fname "e.e." :gender "M" :color "light-gray" :bdate (date 1894 10 14)}
   {:lname "Plant" :fname "Robert" :gender "M" :color "Blue" :bdate (date 1948 8 20)}
   {:lname "Metheny" :fname "Pat" :gender "M", :color "green" :bdate (date 1954 8 12)}
   {:lname "O'Conner" :fname "Sinead" :gender "F" :color "cyan" :bdate (date 1966 12 8)}
   {:lname "McLachlan" :fname "Sarah" :gender "F" :color "Green" :bdate (date 1968 1 28)}
   {:lname "McLachlan" :fname "Sarah" :gender "F" :color "Green" :bdate (date 1968 1 28)}
   {:lname "DeGraw" :fname "Gavin" :gender "M" :color "light-blue" :bdate (date 1977 2 4)}
   {:lname "Bailey-Rae" :fname "Corinne" :gender "F" :color "RED" :bdate (date 1979 2 26)}
   {:lname "Puth" :fname "Charlie" :gender "M" :color "black" :bdate (date 1991 12 2)}])

(def lname-sorted-check  ; Note: Descending
  [{:lname "Puth" :fname "Charlie" :gender "M" :color "black" :bdate (date 1991 12 2)}
   {:lname "Plant" :fname "Robert" :gender "M" :color "Blue" :bdate (date 1948 8 20)}
   {:lname "O'Conner" :fname "Sinead" :gender "F" :color "cyan" :bdate (date 1966 12 8)}
   {:lname "Metheny" :fname "Pat" :gender "M", :color "green" :bdate (date 1954 8 12)}
   {:lname "McLachlan" :fname "Sarah" :gender "F" :color "Green" :bdate (date 1968 1 28)}
   {:lname "McLachlan" :fname "Sarah" :gender "F" :color "Green" :bdate (date 1968 1 28)}
   {:lname "DeGraw" :fname "Gavin" :gender "M" :color "light-blue" :bdate (date 1977 2 4)}
   {:lname "cummings" :fname "e.e." :gender "M" :color "light-gray" :bdate (date 1894 10 14)}
   {:lname "Bailey-Rae" :fname "Corinne" :gender "F" :color "RED" :bdate (date 1979 2 26)}])
   
(deftest sort-empty-test
  (let [parsed-empty (core/parse (fp "empty.txt"))]
    (testing "Testing Empty data"
      (is (= empty-check (core/sort-gender parsed-empty)))
      (is (= empty-check (core/sort-bdate parsed-empty)))
      (is (= empty-check (core/sort-lname parsed-empty))))))

(deftest sort-presorted-test
  (let [parsed-presorted (core/parse (fp "pipe-presorted.txt")
                                     (fp "comma-presorted.txt")
                                     (fp "space-presorted.txt"))]
    (testing "Presorted order file input data"
      (is (= gender-sorted-check (core/sort-gender parsed-presorted)))
      (is (= bdate-sorted-check (core/sort-bdate parsed-presorted)))
      (is (= lname-sorted-check (core/sort-lname parsed-presorted))))))

(deftest sort-reversed-test
  (let [parsed-reversed (core/parse (fp "pipe-reversed.txt")
                                    (fp "comma-reversed.txt")
                                    (fp "space-reversed.txt"))]
    (testing "Reversed order file input data"
      (is (= gender-sorted-check (core/sort-gender parsed-reversed)))
      (is (= bdate-sorted-check (core/sort-bdate parsed-reversed)))
      (is (= lname-sorted-check (core/sort-lname parsed-reversed))))))

(deftest sort-shuffled-test
  (let [parsed-shuffled (core/parse (fp "pipe-shuffled.txt")
                                    (fp "comma-shuffled.txt")
                                    (fp "space-shuffled.txt"))]
    (testing "Shuffled order file input data"
      (is (= gender-sorted-check (core/sort-gender parsed-shuffled)))
      (is (= bdate-sorted-check (core/sort-bdate parsed-shuffled)))
      (is (= lname-sorted-check (core/sort-lname parsed-shuffled))))))

(deftest ^:disabled sort-errors-test
  (let [parsed-errors (core/parse (fp "errors.txt"))]
    nil))

(deftest ^:disabled next-test
  (testing "temporary"
    (is (= 1 2))))
