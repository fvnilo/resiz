(defproject resiz "0.1.0-SNAPSHOT"
  :description "A simple API to return resized versions of known images"
  :url "https://github.com/nylo-andry/resiz"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.2"]
                 [image-resizer "0.1.6"]]
  :plugins [[lein-ring "0.8.13"]]
  :resource-paths ["resources"]
  :ring {:handler resiz.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
