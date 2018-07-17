(ns tdd-trainer.services.session
  (:require [tdd-trainer.data :as data]))

(defn create-session [start-time]
  "Initialises a session for a period of TDD development"
  (let [initial-session {:session-id 300
           :snapshots nil}]

    (data/add-session! initial-session)
    initial-session))

(defn add-snapshot [session-id snapshot]
  (data/add-snapshot! session-id snapshot))


