(ns tdd-trainer.data)

(def session-data (atom nil))


(defn add-session! [session]
  (let [session-id (:session-id session)]
    (swap! session-data assoc session-id session)))
