(ns resiz.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [resiz.handler :refer :all]))

(deftest validations
  (testing "the number-string parter"
    (is (true? (number-string? "23")))
    (is (false? (number-string? "foo"))))

  (testing "the valid? function"
    (is (true? (valid? "1" "2"))
    (is (false? (valid? "1" "a"))
    (is (false? (valid? "a" "1")))))))

(deftest resizer
  (testing "the resizer"
    (is (not (nil? (resize-image "madagascar-morondava.jpeg" 1 1))))))

(deftest routes
  (testing "the image resize route"
    (let [response (app (mock/request :get "/1/1"))]
      (is (= (:status response) 200)))
    (let [response (app (mock/request :get "/a/1"))]
      (is (= (:status response) 400)))
    (let [response (app (mock/request :get "/1/a"))]
      (is (= (:status response) 400))))

  (testing "the not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
