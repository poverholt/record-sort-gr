(ns record-sort-gr.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as str])
  (:import [java.lang StringBuffer]
           [java.text SimpleDateFormat]
           [java.util Date GregorianCalendar]))


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

(defn str->gender [s]
  (cond
    (empty? s) gender-error
    (str-start-compare s "Female") "F"
    (str-start-compare s "Male") "M"
    :else gender-error))


(def date-format (SimpleDateFormat. "MM/dd/yyyy"))

(def date-error (Date. 0))

(defn str->bdate
  [s]
  (try
    (.parse date-format s)
    (catch Exception e date-error)))

(defn- strip-leading-date-0s
  [s]
  (-> s
      (str/replace #"^0" "")
      (str/replace #"/0" "/")))

(defn bdate->str [date] (strip-leading-date-0s (.format date-format date)))


(defn _line->rec
  [line]
  (zipmap [:lname :fname :gender :color :bdate]
          (-> line
              str/trim
              (str/split #"\s*[,|\s]\s*"))))

(defn line->rec
  [line]
  (-> line
      _line->rec
      (update :gender #(str->gender %))
      (update :bdate #(str->bdate %))
      ))

(defn file->lines
  [fname]
  (remove str/blank? (with-open [rdr (io/reader fname)]
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

(defn rec->str
  [rec]
  (str (:lname rec) ", " (:fname rec) ", " (:gender rec) ", " (:color rec) ", " (bdate->str (:bdate rec))))


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

(defn -main
  "Parses the original shuffled test data files, then prints in three orders.
  1. gender-lname ascending, 2. bdate ascending, 3. lname descending."
  [& args]
  (let [fnames ["test/clj/record_sort_gr/fs/pipe-shuffled.txt"
                "test/clj/record_sort_gr/fs/comma-shuffled.txt"
                "test/clj/record_sort_gr/fs/space-shuffled.txt"]
        recs (files->recs fnames)
        gender-sorted-lines (map rec->str (sort-gender recs))
        bdate-sorted-lines (map rec->str (sort-bdate recs))
        lname-sorted-lines (map rec->str (sort-lname recs))]
    (doseq [line (concat ["" "Sorted by Gender" "----------------"]
                         gender-sorted-lines
                         ["" "Sorted by Birthdate" "-------------------"]
                         bdate-sorted-lines
                         ["" "Sorted by Lastname Descending" "-----------------------------"]
                         lname-sorted-lines)]
      (println line))))


(file->recs "test/clj/record_sort_gr/fs/pipe-shuffled.txt")
