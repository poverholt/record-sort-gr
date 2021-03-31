(ns record-sort-gr.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as str])
  (:import [java.util Date]
           [java.text SimpleDateFormat ParsePosition]))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

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
  [& fnames] [{}])

(defn sort-gender [records] nil)

(defn sort-bdate [records] nil)

(defn sort-lname [records] nil)


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

(defn ->gender [s]
  (cond
    (str-start-compare s "Female") "F"
    (str-start-compare s "Male") "M"
    :else "X"))

(defn format-gender
  [rec]
  (update rec :gender #(->gender %)))

;; (.after (Date. 2000 1 1) (Date. 1990 1 1))

(defn ->bdate [s]
  (.parse (SimpleDateFormat. "MM/dd/yyyy") s (ParsePosition. 0)))

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
  (with-open [rdr (io/reader fname)]
    (reduce conj [] (line-seq rdr))))

(defn file->recs
  [fname]
  (map line->rec (file->lines fname)))

(defn files->recs
  [fnames]
  (mapcat #(map line->rec (file->lines %)) fnames))

(file->recs "./test/clj/record_sort_gr/fs/pipe-random.txt")

(files->recs ["./test/clj/record_sort_gr/fs/pipe-random.txt"
              "./test/clj/record_sort_gr/fs/comma-random.txt"
              "./test/clj/record_sort_gr/fs/space-random.txt"])
