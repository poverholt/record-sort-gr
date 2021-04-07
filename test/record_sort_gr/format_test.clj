(ns record-sort-gr.format-test
  (:require [record-sort-gr.format :as format]
            [record-sort-gr.bdate-test :as bdate-test])
  (:use clojure.test))

(deftest rec-str-test
  (let [rec {:lname "Newton" :fname "Isaac" :gender "M" :color "Red" :bdate (bdate-test/date 1643 1 4)}]
    (testing "Testing rec->str"
      (is (= (format/rec->str rec) "Newton, Isaac, M, Red, 1/4/1643")))))
