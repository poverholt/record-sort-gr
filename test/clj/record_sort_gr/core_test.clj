(ns record-sort-gr.core-test
  (:require [record-sort-gr.core :as core])
  (:use clojure.test))

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
;;;; * Delimiters should allow user to have empty fields.

(deftest ^:disabled next-test
  (testing "temporary"
    (is (= 1 2))))
