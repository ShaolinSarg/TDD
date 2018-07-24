(ns tdd-trainer.routes.session
  (:require [compojure.core :refer [defroutes POST GET]]
            [ring.util.response :refer [content-type created status response]]
            [taoensso.timbre :refer [debug info warn]]
            [tdd-trainer.services.session :as service]
            [tdd-trainer.validators.core :refer [validate-time]]
            [cheshire.core :refer [generate-string]]
            [cheshire.generate :refer [add-encoder encode-str]]))

(defn- create-session [start-time]
  (do
    (info "calling add session service")
    (if-let [valid-start (validate-time start-time)]
      (let [initial-session (service/create-session valid-start)
            session-id (:session-id initial-session)]

        (info (str "created session: " session-id))
        (content-type
         (created (str "/sessions/" session-id))
         "application/json"))

      (do
        (warn (str "could not create session"))
        (status (response "bad time") 400)))))

(defn- add-snapshot [session-id timestamp fail-count fail-names]
  (do
    (info (str "calling add snapshot service for session: " session-id))
    (if-let [valid-time (validate-time timestamp)]
      (let [snapshot {:timestamp valid-time
                      :failing-test-count fail-count
                      :failing-test-names fail-names}]

        (if-let [resp (service/add-snapshot (Integer/parseInt session-id) snapshot)]
          (created (str "/sessions/" session-id "/snapshots"))
          (do
            (info "could not add snapshot")
            (status (response "no session found") 404))))
      (do
        (warn (str "could not add snapshot"))
        (status (response "bad time") 400)))))

(defn- get-session-by-id [id]
  (do
    (info (str "calling get session data for session: " id))
    (-> (service/get-session (Integer/parseInt id))
        generate-string
        response
        (content-type "application/json"))))

(add-encoder org.joda.time.DateTime encode-str)

(defroutes session-routes
  (POST "/sessions" request (let [t (get-in request [:body "timestamp"])]
                              (create-session t)))

  (POST "/sessions/:id/snapshots" request (let [id (get-in request [:route-params :id])
                                                timestamp (get-in request [:body "timestamp"])
                                                failingTestCount (get-in request [:body "failingTestCount"])
                                                failingTestNames (get-in request [:body "failingTestNames"])]

                                            (add-snapshot id timestamp failingTestCount failingTestNames)))

  (GET "/sessions/:id" [id] (get-session-by-id id))

  (GET "/sessions/:id/stats" request {:status 299 :body "blam"}))

