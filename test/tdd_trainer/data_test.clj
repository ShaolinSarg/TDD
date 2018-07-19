(ns tdd-trainer.data-test
  (:require [tdd-trainer.data :as sut]
            [clojure.test :refer [deftest testing is use-fixtures]]))


(def snapshot
  {:snapshot-timestamp "2018-07-04 12:20:30"
   :failing-test-count 0
   :failing-test-names []})


(def initial-session
  {:session-id 200 :snapshots nil})


(defn clear-state [t]
  (reset! sut/session-data {})
  (t))


(use-fixtures :each clear-state)

(deftest data-tests

  (testing "Add session"
    (testing "should add another session to the store"
      (do
        (is (= 0 (count @sut/session-data)))
        (sut/add-session! initial-session)
        (is (= 1 (count @sut/session-data))))))

  (testing "add-snapshot"
    (testing "should add a snapshot to the current session"
      (do
        (sut/add-session! initial-session)
        (is (= 0 (count (get-in @sut/session-data [200 :snapshots]))))
        (sut/add-snapshot! 200 snapshot)
        (is (= 1 (count (get-in @sut/session-data [200 :snapshots]))))
        (is (= snapshot (first (get-in @sut/session-data [200 :snapshots]))))))
    (testing "should return nil if the session-id does not exist"
      (is (nil? (sut/add-snapshot! 999 snapshot))))))
