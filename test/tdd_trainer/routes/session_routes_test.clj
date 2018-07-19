(ns tdd-trainer.routes.session-routes-test
  (:require [clojure.test :refer [deftest is testing]]
            [ring.mock.request :as mock]
            [tdd-trainer.routes.core :as sut]
            [cheshire.core :refer [generate-string]]
            [clj-time.core :as t]))

(deftest session-route-tests

  (testing "POST /session"
    (testing "returns a success response"
      (with-redefs-fn {#'tdd-trainer.services.session/create-session (fn [t] {:session-id 400})}
        #(let [response (sut/site (-> (mock/request :post "/sessions")
                                      (mock/json-body {:timestamp "2018-04-05 23:54:52"})))]

          (is (= 201 (:status response)))
          (is (= "application/json" (get-in response [:headers "Content-Type"])))
          (is (= "http://localhost/sessions/400" (get-in response [:headers "Location"]))))))

    (testing "returns a bad request response"
      (testing "when the date time is invalid"
        (is (= 400 (:status (-> (sut/site (mock/request :post "/sessions"))
                                (mock/json-body {:timestamp "something"}))))))))

  (testing "GET /sessions/:id"
    (testing "returns a session"
      (let [session {:session-id 400
                     :snapshots [{:timestamp (t/date-time 2018 3 5 13 34 45)}]}]
        (with-redefs-fn {#'tdd-trainer.services.session/get-session (fn [t] session)}
          #(let [response (sut/site (mock/request :get "/sessions/400"))]

             (is (= 200 (:status response)))
             (is (= "application/json" (get-in response [:headers "Content-Type"])))
             (is (= (generate-string session) (:body response))))))))


  (testing "POST /sessions/:id/snapshots"
    (testing "returns the correct status"
      (with-redefs-fn {#'tdd-trainer.services.session/add-snapshot (fn [session-id snapshot] {:session-id 400})}
        #(is (= 201 (:status (sut/site (-> (mock/request :post "/sessions/300/snapshots")
                                           (mock/json-body {:timestamp "2018-04-05 23:54:52"
                                                            :failingTestCount 0
                                                            :failingTestNames []}))))))))

    (testing "returns not found"
      (testing "when the session id does not exist"
        (with-redefs-fn {#'tdd-trainer.services.session/add-snapshot (fn [session-id snapshot] nil)}
          #(is (= 404 (:status (sut/site (-> (mock/request :post "/sessions/200/snapshots")
                                             (mock/json-body {:timestamp "2018-04-05 23:54:52"
                                                              :failingTestCount 0
                                                              :failingTestNames []})))))))))))
