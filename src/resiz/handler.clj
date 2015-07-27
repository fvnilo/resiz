(ns resiz.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [image-resizer.resize :refer :all]
            [image-resizer.format :as format]
            [image-resizer.scale-methods :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as response]))

(defn resize-image
  [image]

  (def img (-> image
               (io/resource)
               (.getPath)
               (io/file)))

  (format/as-stream ((resize-fn 500 500 speed) img) "jpg"))

(defroutes app-routes
  (GET "/" []
    (def resized-image (resize-image "madagascar-morondava.jpeg"))
    (-> (response/response resized-image)
        (response/content-type "image/jpeg")))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
