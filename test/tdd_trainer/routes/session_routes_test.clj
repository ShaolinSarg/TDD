(ns tdd-trainer.routes.session-routes-test
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
                                                         [:headers "Location"]))))))

    (testing "POST /session/:id/snapshots"
      (testing "returns the correct status"
        (with-redefs-fn {#'tdd-trainer.services.session/add-snapshot (fn [session-id snapshot] {:session-id 400})}
          #(is (= 201 (:status (sut/site (-> (mock/request :post "/session/300/snapshots")
                                             (mock/json-body {:timestamp "2018-04-05 23:54:52"
                                                              :failingTestCount 0
                                                              :failingTestNames []}))))))))

      (testing "returns not found"
        (testing "when the session id does not exist"
          (with-redefs-fn {#'tdd-trainer.services.session/add-snapshot (fn [session-id snapshot] nil)}
            #(is (= 404 (:status (sut/site (-> (mock/request :post "/session/200/snapshots")
                                               (mock/json-body {:timestamp "2018-04-05 23:54:52"
                                                                :failingTestCount 0
                                                                :failingTestNames []})))))))))))

  (testing "endpoints should return a bad request response"
    (testing "POST /session"
      (testing "when the date time is invalid"
        (is (= 400 (:status (-> (sut/site (mock/request :post "/session"))
                                (mock/json-body {:timestamp "something"}))))))))
  )

(deftest validate-time-tests
  (testing "return nil for invalid time"
    (is (nil? (tdd-trainer.validators.core/validate-time "1234"))))
  (testing "return a dateTime for a valid time"
    (is (= (t/date-time 2018 3 5 13 34 45)
           (tdd-trainer.validators.core/validate-time "2018-03-05 13:34:45")))))

      
