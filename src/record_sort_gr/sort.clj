(ns record-sort-gr.sort
  (:require [clojure.string :as str]))

(defn gender
  "Sort by gender (females before males), then by last name ascending"
  [recs]
  (letfn [(gender-lname-cmp [a b]
            (let [c (compare (:gender a) (:gender b))]
              (if (not= c 0)
                c
                (let [c (compare (str/lower-case (:lname a)) (str/lower-case (:lname b)))]
                  c))))]
    (sort gender-lname-cmp recs)))

(defn bdate
  "Sort by birth date ascending."
  [recs]
  (letfn [(date-cmp [a b] (.compareTo a b))]
    (sort-by :bdate date-cmp recs)))

(defn lname
  "Sort by last name descending"
  [recs]
  (letfn [(rev-cmp [a b] (compare (str/lower-case b) (str/lower-case a)))]
    (sort-by :lname rev-cmp recs)))
