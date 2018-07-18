(ns tdd-trainer.routes.session
  (:require [compojure.core :refer [defroutes POST GET]]
            [ring.util.response :refer [content-type created status response]]
            [taoensso.timbre :refer [debug info warn]]
            [tdd-trainer.services.session :as service]
            [tdd-trainer.validators.core :refer [validate-time]]))

(defn- create-session [start-time]
  (do
    (info "calling add session service")
    (if-let [valid-start (validate-time start-time)]
      (let [initial-session (service/create-session valid-start)
            session-id (:session-id initial-session)]

        (info (str "created session: " session-id))
        (-> (created (str "/session/" session-id))
            (content-type "application/json")))

      (do
        (warn (str "could not create session"))
        (status (response "bad time") 400)))))

(defn- add-snapshot [session-id snapshot]
  (do
    (info (str "calling add snapshot service for session: " session-id))
    (if-let [resp (service/add-snapshot session-id snapshot)]
      (created (str "/session/" session-id "/snapshots"))
      (do
        (info "could not add snapshot")
        (status (response "no session found") 404)))))


(defroutes session-routes
  (POST "/session" request (let [t (get-in request [:body "timestamp"])]
                             (create-session t)))

  (POST "/session/:id/snapshots" request (let [id (get-in request [:route-params :id])]
                                           (add-snapshot id {})))

  (GET "/session/:id/stats" request {:status 299 :body "blam"}))

