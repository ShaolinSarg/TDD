(ns tdd-trainer.validators.core-test
  (:require [clj-time.core :as t]
            [clojure.test :refer [deftest is testing]]
            [tdd-trainer.validators.core :as sut]))

(deftest validate-time-tests
  (testing "return nil for invalid time"
    (is (nil? (tdd-trainer.validators.core/validate-time "1234"))))
  (testing "return a dateTime for a valid time"
    (is (= (t/date-time 2018 3 5 13 34 45)
           (tdd-trainer.validators.core/validate-time "2018-03-05 13:34:45")))))
