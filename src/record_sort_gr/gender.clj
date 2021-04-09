(ns record-sort-gr.gender
  (:require [clojure.string :as str]))

(defn- str-start-compare
  "Check if all of s is at the start of valid. The comparison is case insensitive. It fails if s is empty or longer
  than valid."
  [s valid]
  (if (or (= 0 (count s)) (> (count s) (count valid)))
    false
    (let [valid (str/lower-case (subs valid 0 (count s)))
          s (str/lower-case s)]
      (= s valid))))

(def error "GENDER-ERROR")

(defn str->gender [s]
  (cond
    (empty? s) error
    (str-start-compare s "Female") "F"
    (str-start-compare s "Male") "M"
    :else error))

