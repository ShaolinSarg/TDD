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
                       [:headers "Content-Type"]))))

      (testing "returns a location header for the new resource"
        (with-redefs-fn {#'tdd-trainer.services.session/create-session (fn [t] {:session-id 400})}
          #(is (= "/session/400" (get-in (sut/all-routes (mock/request :post "/session"))
                                        [:headers "Location"]))))))))
