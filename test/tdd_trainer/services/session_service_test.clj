(ns tdd-trainer.services.session-service-test
  (:require  [tdd-trainer.services.session :as sut]
             [clojure.test :refer [deftest testing is]]
             [clj-time.core :as t]))

(def snapshot {:timestamp (t/date-time 2018 7 5 3 3 9) :failed 0 :names []})


(deftest session-service-tests
  (with-redefs-fn {#'metrics.histograms/update! (fn [_ _] nil)}
    #(do (testing "create-session should"
           (testing "return a session id"
             (is (= 300 (:session-id (sut/create-session (t/date-time 2018 7 5 3 3 4))))))
           (testing "return an empty snapshot sequence"
             (is (nil? (:snapshots (sut/create-session (t/date-time 2018 7 5 3 3 9)))))))

         (testing "add-snapshot should"
           (testing "retun the updated session when the session exists"
             (let [initial-session (sut/create-session (t/date-time 2018 7 5 3 3 4))
                   snapshot-session (sut/add-snapshot 300 snapshot)]
 
               (is (= 1 (count (get-in snapshot-session [300 :snapshots]))))))

           (testing "add the time since the last save"
             (do
               (sut/create-session (t/date-time 2018 7 5 3 3 4))
               (is (= 5 (:save-gap (first (get-in (sut/add-snapshot 300 {:timestamp (t/date-time 2018 7 5 3 3 9)}) [300 :snapshots])))))))

           (testing "retun nil when the session id is not found"
             (is (nil? (sut/add-snapshot 400 snapshot))))))))
