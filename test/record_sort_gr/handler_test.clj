(ns record-sort-gr.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [record-sort-gr.core :refer :all])) ;; TODO: app was moved to core from handler! Update tests.

(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "Test only"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
