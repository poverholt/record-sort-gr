(ns record-sort-gr.gender-test
  (:require [record-sort-gr.gender :as gdr])
  (:use clojure.test))

(deftest gender-test
  (testing "Tesing str->gender"
    (is (= (gdr/str->gender "Male") "M"))
    (is (= (gdr/str->gender "F") "F"))
    (is (= (gdr/str->gender "Fem") "F"))
    (is (= (gdr/str->gender "Female") "F"))
    (is (= (gdr/str->gender "Female ") "X"))
    (is (= (gdr/str->gender "Femalesque") "X"))
    (is (= (gdr/str->gender "BAD") "X"))
    (is (= (gdr/str->gender " ") "X"))
    (is (= (gdr/str->gender "") "X"))
    (is (= (gdr/str->gender nil) "X"))))
