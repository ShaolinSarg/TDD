(ns tdd-trainer.services.session
  (:require [clj-time.core :as t]
            [metrics.histograms :as histogram]
            [taoensso.timbre :refer [debug info]]
            [tdd-trainer.data :as data]
            [tdd-trainer.metrics.core :refer [failing-test-count save-gap]]))

(defn diff-times
  [start end]
  (do
    (debug (str "start is: " start))
    (debug (str "end is : " end))
    (t/in-seconds (t/interval start end))))

(defn create-session
  "Initialises a session for a period of TDD development"
  [start-time]
  (let [initial-session {:session-id 300
                         :timestamp start-time
                         :snapshots nil}]

    (info (str "created session with id: " (:session-id initial-session)))

    (data/add-session! initial-session)
    (debug (str "persisted session to store, returning: " initial-session))

    initial-session))

(defn get-session [session-id]
  (do
    (info (str "getting session for id: " session-id))
    (data/get-session-data session-id)))

(defn add-snapshot [session-id snapshot]
  (if-let [session (get-session session-id)]
    (let [session-snapshots (:snapshots (get-session session-id))
          last-timestamp (if-let [latest-snapshot (first session-snapshots)]
                           (:timestamp latest-snapshot)
                           (:timestamp session))
          time-gap (diff-times last-timestamp (:timestamp snapshot))
          updated-snapshot (assoc snapshot :save-gap time-gap)]

      (info (str "adding snapshot to session: " session-id))
      (histogram/update! failing-test-count (:failing-test-count updated-snapshot))
      (histogram/update! save-gap time-gap)
      (data/add-snapshot! session-id updated-snapshot)))) 


