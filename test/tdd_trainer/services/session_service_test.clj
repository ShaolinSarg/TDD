(ns tdd-trainer.services.session-service-test
  (:require  [tdd-trainer.services.session :as sut]
             [clojure.test :refer [deftest testing is]]))

(def snapshot {:time 1234 :failed 0 :names []})


(deftest session-service-tests
  (testing "create-session should"
    (testing "return a session id"
      (is (= 300 (:session-id (sut/create-session "2018-07-05 03:03:04")))))
    (testing "return an empty snapshot sequence"
      (is (nil? (:snapshots (sut/create-session "2018-07-05 03:03:04"))))))

  (testing "add-snapshot should"
    (testing "retun the updated session when the session exists"
      (do
        (sut/create-session "2018-07-05 03:03:04")
        (is (= 1 (count (get-in (sut/add-snapshot 300 snapshot) [300 :snapshots]))))))

    (testing "retun nil when the session id is not found"
      (is (nil? (sut/add-snapshot 400 snapshot))))))
