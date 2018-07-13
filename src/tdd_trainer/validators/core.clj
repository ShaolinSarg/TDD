(ns tdd-trainer.validators.core
  (:require [taoensso.timbre :refer [debug info]]
            [clj-time.format :as f]))


(def json-datetime-formatter (f/formatters :mysql))

(defn validate-time [t]
  (try
    (f/parse json-datetime-formatter t)

    (catch Exception e
      (info (str "Invalid start time: " t))
      nil)))
