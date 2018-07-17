(ns tdd-trainer.data)

;; {:session-id 300
;;  :snapshots [{:snapshot-timestamp "2018-07-04 12:20:30"
;;               :failing-test-count 0
;;               :failing-test-names []}]}


(def session-data (atom {}))


(defn add-session! [session]
  (let [session-id (:session-id session)]
    (swap! session-data assoc session-id session)))


(defn add-snapshot! [session-id snapshot]
  (if (contains? @session-data session-id)
    (swap! session-data update-in [session-id :snapshots] conj snapshot)
    nil))
