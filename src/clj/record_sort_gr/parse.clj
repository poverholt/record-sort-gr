(ns record-sort-gr.parse
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [record-sort-gr.bdate :as bdate]
            [record-sort-gr.gender :as gdr]))

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
      (update :gender #(gdr/str->gender %))
      (update :bdate #(bdate/str->bdate %))
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
