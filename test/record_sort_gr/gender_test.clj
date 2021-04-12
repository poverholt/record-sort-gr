(ns record-sort-gr.gender-test
  (:require [record-sort-gr.gender :as gdr])
  (:use clojure.test))

(deftest gender-test
  (testing "Tesing str->gender"
    (is (= (gdr/str->gender "Male") "M"))
    (is (= (gdr/str->gender "F") "F"))
    (is (= (gdr/str->gender "Fem") "F"))
    (is (= (gdr/str->gender "Female") "F"))
    (is (= (gdr/str->gender "Female ") gdr/error))
    (is (= (gdr/str->gender "Femalesque") gdr/error))
    (is (= (gdr/str->gender "BAD") gdr/error))
    (is (= (gdr/str->gender " ") gdr/error))
    (is (= (gdr/str->gender "") gdr/error))
    (is (= (gdr/str->gender nil) gdr/error))))
