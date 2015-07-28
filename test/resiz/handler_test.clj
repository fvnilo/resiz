(ns resiz.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [resiz.handler :refer :all]))

(deftest test-app
  (testing "image resize route"
    (let [response (app (mock/request :get "/1/1"))]
      (is (= (:status response) 200)))
    (let [response (app (mock/request :get "/a/1"))]
      (is (= (:status response) 400)))
    (let [response (app (mock/request :get "/1/a"))]
      (is (= (:status response) 400))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
