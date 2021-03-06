(ns tdd-trainer.core
  (:gen-class)
  (:require [org.httpkit.server :refer [run-server]]
            [tdd-trainer.routes.core :refer [site]]
            [taoensso.timbre :refer [debug info]]))


(def server (atom nil))


(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))


(defn -main
  "Starts the TDD Trainer server"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (info (str "Starting Server on port:" port " ..."))
    (reset! server
            (run-server #'site {:port port}))))
