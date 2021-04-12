(ns record-sort-gr.parse-test
  (:require [record-sort-gr.bdate :as bdate]
            [record-sort-gr.parse :as parse]
            [record-sort-gr.bdate-test :as bdate-test])
  (:use clojure.test))

(deftest append-error-test
  (testing "Testing append-error"
    (is (= (parse/append-error {:field "field"} "err") {:field "field" :error "err"}))
    (is (= (parse/append-error {:field "field" :error "err1"} "err2") {:field "field" :error ["err1" "err2"]}))
    (is (= (parse/append-error {:field "field" :error ["err1" "err2"]} "err3") {:field "field" :error ["err1" "err2" "err3"]}))))

(deftest count-filled-test
  (testing "Testing count-filled"
    (is (= (parse/count-filled {}) 0) "No fields")
    (is (= (parse/count-filled {:one "one" :two "" :three "  " :four nil :five "five"}) 2) "Some fields")))

(deftest _line-rec-test
  (testing "Testing _line->rec"
    (is (= (parse/_line->rec "Doe | John | Male | Blue | 12/31/1999") {:lname "Doe" :fname "John" :gender "Male" :color "Blue" :bdate "12/31/1999"}) "Pipe delimiter")
    (is (= (parse/_line->rec "Doe, John, Male, Blue, 12/31/1999") {:lname "Doe" :fname "John" :gender "Male" :color "Blue" :bdate "12/31/1999"}) "Comma delimiter")
    (is (= (parse/_line->rec "Doe John Male Blue 12/31/1999") {:lname "Doe" :fname "John" :gender "Male" :color "Blue" :bdate "12/31/1999"}) "Space delimiter")
    (is (= (parse/_line->rec "Doe|John   |Male| Blue  |  12/31/1999") {:lname "Doe" :fname "John" :gender "Male" :color "Blue" :bdate "12/31/1999"}) "Varying delimiter white space")
    (is (= (parse/_line->rec "  Doe | John | Male | Blue | 12/31/1999  ") {:lname "Doe" :fname "John" :gender "Male" :color "Blue" :bdate "12/31/1999"}) "Surrounding white space")
    (is (= (parse/_line->rec "Doe | John | Male | Blue | 12/31/1999 | Extra") {:lname "Doe" :fname "John" :gender "Male" :color "Blue" :bdate "12/31/1999"}) "Extra field")
    (is (= (parse/_line->rec "Doe | John | Male | Blue") {:lname "Doe" :fname "John" :gender "Male" :color "Blue"}) "Missing last field")
    (is (= (parse/_line->rec "John | Male | Blue 12/31/1999") {:lname "John" :fname "Male" :gender "Blue" :color "12/31/1999"}) "Missing first field")
    (is (= (parse/_line->rec "Doe") {:lname "Doe"}) "One field")
    (is (= (parse/_line->rec "   ") {:lname ""}) "White space only")
    (is (= (parse/_line->rec "") {:lname ""}) "Empty line")))

(deftest line-rec-test
  (testing "Testing line->rec"
    (is (= (parse/line->rec "Doe | John | M | Blue | 12/31/1999") {:lname "Doe" :fname "John" :gender "M" :color "Blue" :bdate (bdate-test/date 1999 12 31)}) "Pipe delimiter")
    (is (= (parse/line->rec "Doe, John, M, Blue, 12/31/1999") {:lname "Doe" :fname "John" :gender "M" :color "Blue" :bdate (bdate-test/date 1999 12 31)}) "Comma delimiter")
    (is (= (parse/line->rec "Doe John M Blue 12/31/1999") {:lname "Doe" :fname "John" :gender "M" :color "Blue" :bdate (bdate-test/date 1999 12 31)}) "Space delimiter")
    (is (= (parse/line->rec "Doe|John   |M| Blue  |  12/31/1999") {:lname "Doe" :fname "John" :gender "M" :color "Blue" :bdate (bdate-test/date 1999 12 31)}) "Varying delimiter white space")
    (is (= (parse/line->rec "  Doe | John | M | Blue | 12/31/1999  ") {:lname "Doe" :fname "John" :gender "M" :color "Blue" :bdate (bdate-test/date 1999 12 31)}) "Surrounding white space")
    (is (= (parse/line->rec "Doe | John | M | Blue | 12/31/1999 | Extra") {:lname "Doe" :fname "John" :gender "M" :color "Blue" :bdate (bdate-test/date 1999 12 31)}) "Extra field")
    (is (= (:error (parse/line->rec "Doe | John | M | Blue")) "Invalid syntax: Expected 5 data fields, but received 4.") "Missing last field")
    (is (= (:error (parse/line->rec "John | M | Blue 12/31/1999")) "Invalid syntax: Expected 5 data fields, but received 4.") "Missing first field")
    (is (= (:error (parse/line->rec "Doe||m||12/31/1999")) "Invalid syntax: Expected 5 data fields, but received 3.") "Missing middle fields")
    (is (= (:error (parse/line->rec "Doe")) "Invalid syntax: Expected 5 data fields, but received 1.") "One field")
    (is (= (:error (parse/line->rec "   ")) "Invalid syntax: Expected 5 data fields, but received 0.") "White space only")
    (is (= (:error (parse/line->rec "")) "Invalid syntax: Expected 5 data fields, but received 0.") "Empty line")))

(defn fp [fname] (str "test/record_sort_gr/fs/" fname))

(deftest files-recs-test
  (let [parsed-9 (parse/files->recs [(fp "pipe-presorted.txt") (fp "comma-presorted.txt") (fp "space-presorted.txt")])
        parsed-11-errors (parse/files->recs [(fp "errors.txt")])
        parsed-empty (parse/files->recs [(fp "empty.txt")])
        parsed-blank-lines (parse/files->recs [(fp "blank-lines.txt")])]
    (testing "Testing files->recs"
      (is (= (count parsed-9) 9) "Parses 9 valid records")
      (is (= (count parsed-11-errors) 11) "Parses 11 invalid records without throwing exceptions")
      (is (= (count parsed-empty) 0) "Empty files produce 0 records without exceptions")
      (is (= (count parsed-blank-lines) 0) "Blank lines produce 0 records without exceptions"))))
