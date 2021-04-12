(ns record-sort-gr.parse
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [record-sort-gr.bdate :as bdate]
            [record-sort-gr.gender :as gdr]))

(defn append-error
  "Append error field to record. Handles cases where there is currently no error, 1 error or several errors.
  The error field is a list iff there are more than 1 error."
  [rec err-str]
  (let [err (:error rec)]
    (cond
      (nil? err) (assoc rec :error err-str)
      (string? err) (update rec :error list err-str)  
      :else (update rec :error conj err-str))))

(defn standardize-gender
  [rec]
  (let [gender-input (:gender rec)
        rec (update rec :gender #(gdr/str->gender %))]
    (if (= (:gender rec) gdr/error)
      (append-error rec (str "Invalid gender format: " gender-input ". The following case-insensitive values are valid: f, female, m, male."))
      rec)))

(defn standardize-bdate
  [rec]
  (let [date-input (:bdate rec)
        rec (update rec :bdate #(bdate/str->bdate %))]
    (if (= (:bdate rec) bdate/error)
      (append-error rec (str "Invalid date format: " date-input ". Date must be in format mm/dd/yyyy. Single character month and date are allowed."))
      rec)))

(defn empty-check
  "Check for empty fields."
  [rec field-key]
  (if (str/blank? (field-key rec))
    (append-error rec (str "Empty field not allowed: " field-key))
    rec))

(defn count-filled
  "Count non-blank fields."
  [rec]
  (count (remove (fn [[k v]] (str/blank? v)) rec)))

(defn _line->rec
  [line]
  (zipmap [:lname :fname :gender :color :bdate]
          (-> line
              str/trim
              (str/split #"\s*[,|\s]\s*"))))

(defn line->rec
  "Attempts to return a map with the 5 record fields. The gender and bdate fields will be standardized.

   If errors were found, an :error field is added. This could happen if the line is nil/empty, if 5 fields
   were not found, if gender is invalid, if bdate is invalid. The :error field may be a single error string
   or a list of error strings. Extra fields are ignored and do not cause an error."
  [line]
  (if (= nil line)
    {:error "Invalid syntax: No data."}
    (let [rec (_line->rec line)
          cnt (count-filled rec)]
      (if (not= cnt 5)
        (assoc rec :error (str "Invalid syntax: Expected 5 data fields, but received " cnt "."))
        (-> rec
            (empty-check :lname)
            (empty-check :fname)
            (empty-check :color)
            standardize-gender
            standardize-bdate)))))

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
