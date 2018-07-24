(ns tdd-trainer.routes.core
  (:require [compojure.core :refer [defroutes]]
            [tdd-trainer.routes.session :refer [session-routes]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-body]]
            [metrics.ring.instrument :refer [instrument]]
            [metrics.ring.expose :refer [expose-metrics-as-json]]))


(defroutes all-routes
  session-routes)


(def site
  (-> all-routes
      (wrap-defaults api-defaults)
      wrap-json-body
      instrument
      expose-metrics-as-json))

