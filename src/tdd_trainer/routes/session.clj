(ns tdd-trainer.routes.session
  (:require [compojure.core :refer [defroutes POST]]
            [ring.util.response :refer [content-type created response]]
            [taoensso.timbre :refer [debug info]]))


(defn- create-session []
  (do
    (info "calling add session service")
    (content-type (created "/session/123")
                  "application/json")))


(defroutes session-routes
  (POST "/session" [] (create-session)))
