(ns record-sort-gr.bdate-test
  (:require [record-sort-gr.bdate :as bdate])
  (:use clojure.test)
  (:import [java.util Date GregorianCalendar]))

(defn date
  "Returns a java Date object given year, month, day as integers. Input month is 1-based, meaning January is month 1."
  [year month day]
  (.getTime (GregorianCalendar. year (dec month) day)))

(deftest str-bdate-test ; NOTE: If called "str->bdate-test" with "->", lein test :only won't run!!!???
  (testing "Testing str->bdate"
    (is (= (bdate/str->bdate "1/4/1643") (date 1643 1 4)))
    (is (= (bdate/str->bdate "12/31/1999") (date 1999 12 31)))
    (is (= (bdate/str->bdate " 12/31/1999 ") (date 1999 12 31)))
    (is (= (bdate/str->bdate "12-31-1999") bdate/error))
    (is (= (bdate/str->bdate "31/12/1999") bdate/error))
    (is (= (bdate/str->bdate "1999/12/31") bdate/error))
    (is (= (bdate/str->bdate "BAD") bdate/error))
    (is (= (bdate/str->bdate " ") bdate/error))
    (is (= (bdate/str->bdate "") bdate/error))
    (is (= (bdate/str->bdate nil) bdate/error))))

(deftest bdate-str-test
  (testing "Testing bdate->str"
    (is (= (bdate/bdate->str (date 1643 1 4)) "1/4/1643")) ; Requirements indicate this must not be 01/04/1643
    (is (= (bdate/bdate->str (date 1999 12 31)) "12/31/1999"))))
