(ns tdd-trainer.watcher.core
  (:require [hawk.core :as hawk]
            [taoensso.timbre :refer [debug]]))

;;(defn file-filter)


(defn initialise-watcher!
  [path]
  (hawk/watch! [{:paths [path]
                 :filter hawk/file?
                 :handler (fn [ctx e]
                            (debug (str "event: " e)))}]))
