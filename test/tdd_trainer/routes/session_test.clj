(ns tdd-trainer.routes.session-test
  (:require [tdd-trainer.routes.core :as sut]
            [clojure.test :refer [deftest testing is]]
            [ring.mock.request :as mock]))

(deftest session-route-tests
  (testing "endpoints should return a valid success response"

    (testing "POST /session"
      (testing "returns the correct status"
        (is (= 201 (:status (sut/all-routes (mock/request :post "/session"))))))

      (testing "returns the correct content type"
        (is (= "application/json"
               (get-in (sut/all-routes (mock/request :post "/session"))
                       [:headers "Content-Type"])))))))
