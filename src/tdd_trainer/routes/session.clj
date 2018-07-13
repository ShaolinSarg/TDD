(ns tdd-trainer.routes.session
  (:require [compojure.core :refer [defroutes POST]]
            [ring.util.response :refer [content-type created status response]]
            [taoensso.timbre :refer [debug info]]
            [tdd-trainer.services.session :as service]
            [clj-time.format :as f]))


(def json-datetime-formatter (f/formatters :mysql))


(defn validate-time [t]
  (try
    (f/parse json-datetime-formatter t)

    (catch Exception e
      (info (str "Invalid start time: " t))
      nil)))


(defn- create-session [start-time]
  (do
    (info "calling add session service")
    (if-let [valid-start (validate-time start-time)]
      (let [initial-session (service/create-session "sdfsd")
            session-id (:session-id initial-session)]

        (-> (created (str "/session/" session-id))
            (content-type "application/json")))
      (status (response "bad time") 400))))

(defroutes session-routes
  (POST "/session" request (let [t (get-in request [:body "timestamp"])]
                               (info (str "time is :" t))
                               (info (str "request is :" request))
                               (create-session t))))
