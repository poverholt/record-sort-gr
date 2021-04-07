(ns record-sort-gr.sort-test
  (:require [record-sort-gr.parse :as parse]
            [record-sort-gr.sort :as sort]
            [record-sort-gr.bdate-test :as btest]
            [record-sort-gr.parse-test :as ptest])
  (:use clojure.test))

(def empty-check '())

(def gender-sorted-check
  [{:lname "Bailey-Rae" :fname "Corinne" :gender "F" :color "RED" :bdate (btest/date 1979 2 26)}
   {:lname "McLachlan" :fname "Sarah" :gender "F" :color "Green" :bdate (btest/date 1968 1 28)}
   {:lname "McLachlan" :fname "Sarah" :gender "F" :color "Green" :bdate (btest/date 1968 1 28)}
   {:lname "O'Conner" :fname "Sinead" :gender "F" :color "cyan" :bdate (btest/date 1966 12 8)}
   {:lname "cummings" :fname "e.e." :gender "M" :color "light-gray" :bdate (btest/date 1894 10 14)}
   {:lname "DeGraw" :fname "Gavin" :gender "M" :color "light-blue" :bdate (btest/date 1977 2 4)}
   {:lname "Metheny" :fname "Pat" :gender "M", :color "green" :bdate (btest/date 1954 8 12)}
   {:lname "Plant" :fname "Robert" :gender "M" :color "Blue" :bdate (btest/date 1948 8 20)}
   {:lname "Puth" :fname "Charlie" :gender "M" :color "black" :bdate (btest/date 1991 12 2)}])

(def bdate-sorted-check
  [{:lname "cummings" :fname "e.e." :gender "M" :color "light-gray" :bdate (btest/date 1894 10 14)}
   {:lname "Plant" :fname "Robert" :gender "M" :color "Blue" :bdate (btest/date 1948 8 20)}
   {:lname "Metheny" :fname "Pat" :gender "M", :color "green" :bdate (btest/date 1954 8 12)}
   {:lname "O'Conner" :fname "Sinead" :gender "F" :color "cyan" :bdate (btest/date 1966 12 8)}
   {:lname "McLachlan" :fname "Sarah" :gender "F" :color "Green" :bdate (btest/date 1968 1 28)}
   {:lname "McLachlan" :fname "Sarah" :gender "F" :color "Green" :bdate (btest/date 1968 1 28)}
   {:lname "DeGraw" :fname "Gavin" :gender "M" :color "light-blue" :bdate (btest/date 1977 2 4)}
   {:lname "Bailey-Rae" :fname "Corinne" :gender "F" :color "RED" :bdate (btest/date 1979 2 26)}
   {:lname "Puth" :fname "Charlie" :gender "M" :color "black" :bdate (btest/date 1991 12 2)}])

(def lname-sorted-check  ; Note: Descending
  [{:lname "Puth" :fname "Charlie" :gender "M" :color "black" :bdate (btest/date 1991 12 2)}
   {:lname "Plant" :fname "Robert" :gender "M" :color "Blue" :bdate (btest/date 1948 8 20)}
   {:lname "O'Conner" :fname "Sinead" :gender "F" :color "cyan" :bdate (btest/date 1966 12 8)}
   {:lname "Metheny" :fname "Pat" :gender "M", :color "green" :bdate (btest/date 1954 8 12)}
   {:lname "McLachlan" :fname "Sarah" :gender "F" :color "Green" :bdate (btest/date 1968 1 28)}
   {:lname "McLachlan" :fname "Sarah" :gender "F" :color "Green" :bdate (btest/date 1968 1 28)}
   {:lname "DeGraw" :fname "Gavin" :gender "M" :color "light-blue" :bdate (btest/date 1977 2 4)}
   {:lname "cummings" :fname "e.e." :gender "M" :color "light-gray" :bdate (btest/date 1894 10 14)}
   {:lname "Bailey-Rae" :fname "Corinne" :gender "F" :color "RED" :bdate (btest/date 1979 2 26)}])
   
(deftest sort-empty-test
  (let [parsed-empty (parse/parse (ptest/fp "empty.txt"))]
    (testing "Testing Empty data"
      (is (= empty-check (sort/gender parsed-empty)))
      (is (= empty-check (sort/bdate parsed-empty)))
      (is (= empty-check (sort/lname parsed-empty))))))

(deftest sort-presorted-test
  (let [parsed-presorted (parse/parse (ptest/fp "pipe-presorted.txt")
                                     (ptest/fp "comma-presorted.txt")
                                     (ptest/fp "space-presorted.txt"))]
    (testing "Testing presorted order file input data"
      (is (= gender-sorted-check (sort/gender parsed-presorted)))
      (is (= bdate-sorted-check (sort/bdate parsed-presorted)))
      (is (= lname-sorted-check (sort/lname parsed-presorted))))))

(deftest sort-reversed-test
  (let [parsed-reversed (parse/parse (ptest/fp "pipe-reversed.txt")
                                    (ptest/fp "comma-reversed.txt")
                                    (ptest/fp "space-reversed.txt"))]
    (testing "Testing reversed order file input data"
      (is (= gender-sorted-check (sort/gender parsed-reversed)))
      (is (= bdate-sorted-check (sort/bdate parsed-reversed)))
      (is (= lname-sorted-check (sort/lname parsed-reversed))))))

(deftest sort-shuffled-test
  (let [parsed-shuffled (parse/parse (ptest/fp "pipe-shuffled.txt")
                                    (ptest/fp "comma-shuffled.txt")
                                    (ptest/fp "space-shuffled.txt"))]
    (testing "Testing shuffled order file input data"
      (is (= gender-sorted-check (sort/gender parsed-shuffled)))
      (is (= bdate-sorted-check (sort/bdate parsed-shuffled)))
      (is (= lname-sorted-check (sort/lname parsed-shuffled))))))

(deftest ^:disabled sort-errors-test
  (let [parsed-errors (parse/parse (ptest/fp "errors.txt"))]
    (testing "Testing file errors such as bad date, bad gender, extra field, missing field(s)."
      (is (= gender-sorted-check (sort/gender parsed-errors)))
      (is (= bdate-sorted-check (sort/bdate parsed-errors)))
      (is (= lname-sorted-check (sort/lname parsed-errors))))))
