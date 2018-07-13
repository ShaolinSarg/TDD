(ns tdd-trainer.routes.session-test
  (:require [tdd-trainer.routes.core :as sut]
            [clojure.test :refer [deftest testing is]]
            [ring.mock.request :as mock]
            [tdd-trainer.routes.session :as routes]
            [clj-time.core :as t]))

(deftest session-route-tests
  (testing "endpoints should return a valid success response"

    (testing "POST /session"
      (testing "returns the correct status"
        (is (= 201 (:status (sut/site (-> (mock/request :post "/session")
                                          (mock/json-body {:timestamp "2018-04-05 23:54:52"})))))))

      (testing "returns the correct content type"
        (is (= "application/json"
               (get-in (sut/site (-> (mock/request :post "/session")
                                     (mock/json-body {:timestamp "2018-04-05 23:54:52"})))
                       [:headers "Content-Type"]))))

      (testing "returns a location header for the new resource"
        (with-redefs-fn {#'tdd-trainer.services.session/create-session (fn [t] {:session-id 400})}
          #(is (= "http://localhost/session/400" (get-in (sut/site (-> (mock/request :post "/session")
                                                       (mock/json-body {:timestamp "2018-04-05 23:54:52"})))
                                         [:headers "Location"])))))))

  (testing "endpoints should return a bad request response"
    (testing "POST /session"
      (testing "when the date time is invalid"
        (is (= 400 (:status (-> (sut/site (mock/request :post "/session"))
                                (mock/json-body {:timestamp "something"}))))))))
  )

(deftest validate-time-tests
  (testing "return nil for invalid time"
    (is (nil? (routes/validate-time "1234"))))
  (testing "return a dateTime for a valid time"
    (is (= (t/date-time 2018 3 5 13 34 45)
           (routes/validate-time "2018-03-05 13:34:45")))))
