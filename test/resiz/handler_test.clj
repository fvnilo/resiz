(ns resiz.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [resiz.handler :refer :all]))

(deftest validations
  (testing "the number-string parser"
    (is (true? (number-string? "23")))
    (is (false? (number-string? "foo"))))

  (testing "the dimensions-valid? function"
    (is (true? (dimensions-valid? "1" "2"))
    (is (false? (dimensions-valid? "1" "a"))
    (is (false? (dimensions-valid? "a" "1"))))))

  (testing "the ratio image computing function"
    (is (= (compute-image-file-ratio "clojure.jpeg") 1)))

  (testing "the ratio-valid? function"
    (is (true? (ratio-valid? "clojure.jpeg" "1" "1")))
    (is (false? (ratio-valid? "clojure.jpeg" "2" "1")))))

(deftest resizer
  (testing "the resizer"
    (is (not (nil? (resize-image "clojure.jpeg" 1 1))))))

(deftest file
  (testing "file exists"
    (is (true? (image-exists? "clojure.jpeg"))
    (is (false? (image-exists? "foobar.jpeg"))))))

(deftest routes
  (testing "the image resize route"
    (let [response (app (mock/request :get "/1/1/clojure.jpeg"))]
      (is (= (:status response) 200)))
    (let [response (app (mock/request :get "/a/1/clojure.jpeg"))]
      (is (= (:status response) 400)))
    (let [response (app (mock/request :get "/1/a/clojure.jpeg"))]
      (is (= (:status response) 400)))
    (let [response (app (mock/request :get "/1/1/foo.jpeg"))]
      (is (= (:status response) 404))))

  (testing "the not-found route"
    (let [response (app (mock/request :get "/not-found"))]
      (is (= (:status response) 404)))))
