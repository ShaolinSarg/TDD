(ns tdd-trainer.services.session-service-test
  (:require  [tdd-trainer.services.session :as sut]
             [clojure.test :refer [deftest testing is]]))


(deftest session-service-tests
  (testing "create-session should"
    (testing "return a session id"
      (is (= 300 (:session-id (sut/create-session "2018-07-05 03:03:04")))))))
