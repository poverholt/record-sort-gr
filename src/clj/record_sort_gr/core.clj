(ns record-sort-gr.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as str])
  (:import [java.lang StringBuffer]
           [java.text SimpleDateFormat]
           [java.util Date GregorianCalendar]))

(defn _line->rec
  [line]
  (zipmap [:lname :fname :gender :color :bdate]
          (-> line
              str/trim
              (str/split #"\s*[,|\s]\s*"))))

(defn str-start-compare
  "Check if all of s is at the start of valid. The comparison is case insensitive. It fails if s is empty or longer
  than valid."
  [s valid]
  (if (or (= 0 (count s)) (> (count s) (count valid)))
    false
    (let [valid (str/lower-case (subs valid 0 (count s)))
          s (str/lower-case s)]
      (= s valid))))

(def gender-error "X")

(defn ->gender [s]
  (cond
    (empty? s) gender-error
    (str-start-compare s "Female") "F"
    (str-start-compare s "Male") "M"
    :else gender-error))

(defn format-gender
  [rec]
  (update rec :gender #(->gender %)))

(def date-format (SimpleDateFormat. "MM/dd/yyyy"))

(def date-error (Date. 0))

(defn ->bdate
  [s]
  (if (empty? s)
    date-error
    (.parse date-format s)))

(defn bdate->str [date] (.format date-format date))

(def date (->bdate "2/26/1979"))
(def date-check (.getTime (GregorianCalendar. 1979 (dec 2) 26)))

(= (Date. 1979 2 26) (.parse date-format "2/26/1979"))

(.format date-format (Date. 1979 2 26))

(.getTime (GregorianCalendar. 1979 2 26))

(println (bdate->str date))
(println (bdate->str date-check))
  
(defn format-bdate
  [rec]
  (update rec :bdate #(->bdate %)))

(defn line->rec
  [line]
  (-> line
      _line->rec
      ;;format-gender
      (update :gender #(->gender %))
      ;;format-bdate
      (update :bdate #(->bdate %))
      ))
      
(defn file->lines
  [fname]
  (remove empty? (with-open [rdr (io/reader fname)]
                   (reduce conj [] (line-seq rdr)))))

(defn file->recs
  [fname]
  (map line->rec (file->lines fname)))

(defn files->recs
  [fnames]
  (mapcat #(map line->rec (file->lines %)) fnames))

(defn parse
  "Reads 0 or more files, where each file has one record per line. Each line includes last name, first name, gender,
  favorite color and date of birth, delimited by either pipe (|), comma (,) or space ( ). There delmiter may include
  extra white space as well.

  All records are combined into a single vector with one map entry record per file record. Map entry fields include
  :lname, :fname, :gender, :color and :bdate. Any file records that do not conform also include an :error field and if
  necessary, placeholder information will be put into the other fields.

  All printable characters are allowed in lname and fname. Gender will convert from file text F or Female to text F,
  and likewise for males. Color can be any color in java.awt.Color. bdate will be converted from file format M/D/YYYY
  to map format YYYY-MM-DD.
  "
  [& fnames]
  (files->recs fnames))

(defn sort-gender
  "Sort by gender (females before males), then by last name ascending"
  [recs]
  (letfn [(gender-lname-cmp [a b]
            (let [c (compare (:gender a) (:gender b))]
              (if (not= c 0)
                c
                (let [c (compare (str/lower-case (:lname a)) (str/lower-case (:lname b)))]
                  c))))]
    (sort gender-lname-cmp recs)))

(defn sort-bdate
  "Sort by birth date ascending."
  [recs]
  (letfn [(date-cmp [a b] (.compareTo a b))]
    (sort-by :bdate date-cmp recs)))

(defn sort-lname
  "Sort by last name descending"
  [recs]
  (letfn [(rev-cmp [a b] (compare (str/lower-case b) (str/lower-case a)))]
    (sort-by :lname rev-cmp recs)))

(defn rec-print
  [rec]
  (println (str (:lname rec) ", " (:fname rec) ", " (:gender rec) ", " (:color rec) ", " (bdate->str (:bdate rec)))))

;; (let [d (sort-gender (files->recs ["./test/clj/record_sort_gr/fs/pipe-random.txt"
;;                                    "./test/clj/record_sort_gr/fs/comma-random.txt"
;;                                    "./test/clj/record_sort_gr/fs/space-random.txt"]))]
;;   (map rec-print d))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(parse "test/clj/record_sort_gr/fs/pipe-shuffled.txt"
       ;;"test/clj/record_sort_gr/fs/comma-shuffled.txt"
       "test/clj/record_sort_gr/fs/space-shuffled.txt")

(file->lines "test/clj/record_sort_gr/fs/comma-shuffled.txt")
