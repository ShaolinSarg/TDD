(ns tdd-trainer.core
  (:gen-class)
  (:require [org.httpkit.server :refer [run-server]]
            [taoensso.timbre :refer [info]]
            [tdd-trainer.routes.core :refer [site]]
            [tdd-trainer.watcher.core :refer [initialise-watcher!]]))

(def server (atom nil))


(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))


(defn -main
  "Starts the TDD Trainer server"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))
        watcher (initialise-watcher! "/project/root/")]
    (info (str "Starting Server on port:" port " ..."))
    (reset! server
            (run-server #'site {:port port}))))
