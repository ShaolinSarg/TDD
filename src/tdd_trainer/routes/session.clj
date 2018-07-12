(ns tdd-trainer.routes.session
  (:require [compojure.core :refer [defroutes POST]]
            [ring.util.response :refer [content-type created response]]
            [taoensso.timbre :refer [debug info]]
            [tdd-trainer.services.session :as service]))


(defn- create-session []
  (do
    (info "calling add session service")
    (let [initial-session (service/create-session "")
          session-id (:session-id initial-session)]

      (-> (created (str "/session/" session-id))
          (content-type "application/json")))))

(defroutes session-routes
  (POST "/session" [] (create-session)))
