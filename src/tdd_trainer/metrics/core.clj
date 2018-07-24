(ns tdd-trainer.metrics.core
  (:require [metrics.core :refer [new-registry]]
            [metrics.reporters.graphite :as graphite]
            [metrics.reporters.console :as console]
            [metrics.histograms :refer [defhistogram]])
  (:import [java.util.concurrent TimeUnit])
  (:import [com.codahale.metrics MetricFilter]))

;; (def graphite-metrics-reporter (graphite/reporter {:host (or (System/getenv "GRAPHITE_HOSTNAME") "graphite")
;;                                                    :prefix "tdd-trainer.metrics"
;;                                                    :rate-unit TimeUnit/SECONDS
;;                                                    :duration-unit TimeUnit/MILLISECONDS
;;                                                    :filter MetricFilter/ALL}))

(def metric-registry (new-registry))

(defhistogram metric-registry failing-test-count)

;; (graphite/start graphite-metrics-reporter 10)

(def console-metrics-reporter (console/reporter metric-registry {}))

(console/start console-metrics-reporter 10)
