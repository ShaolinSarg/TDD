(defproject tdd-trainer "0.1.0-SNAPSHOT"

  :description "A web service that tracks the TDD events fired from build tool continous testing"

  :url ""

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [compojure "1.6.1"]
                 [http-kit "2.3.0"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.4.0"]
                 [com.taoensso/timbre "4.10.0"]
                 [metrics-clojure "2.10.0"]
                 [metrics-clojure-ring "2.10.0"]
                 [cheshire "5.8.0"]
                 [clj-time "0.14.4"]]

  :main ^:skip-aot tdd-trainer.core

  :target-path "target/%s"

  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[ring/ring-mock "0.3.2"]]}})
