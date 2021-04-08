(ns record-sort-gr.model-test
  (:require [record-sort-gr.model :as mdl])
  (:use clojure.test))

(defn records-reset-fixture
  "This fixture makes sure the persistent atom of records is clear before and after tests."
  [f]
  (mdl/reset)
  (f)
  (mdl/reset))

(use-fixtures :each records-reset-fixture)

(deftest model-test
  (let [_ (mdl/create-rec {:field "One"})
        _ (mdl/create-rec {:field "Two"})
        _ (mdl/read-recs)
        _ (mdl/create-rec {:field "Three"})
        recs (mdl/read-recs)
        r1-field (-> recs first :field)
        r3-field (-> recs last :field)]
    (testing "Testing model"
      (is (= r1-field "One"))
      (is (= r3-field "Three")))))

