(ns resiz.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [image-resizer.resize :refer :all]
            [image-resizer.format :as format]
            [image-resizer.scale-methods :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as response]))

(defn parse-int [s]
   (Integer. (re-find  #"\d+" s )))

(defn resize-image
  [filename height width]

  (def image-file (-> filename
                     (io/resource)
                     (.getPath)
                     (io/file)))

  (format/as-stream ((resize-fn height width speed) image-file) "jpg"))

(defroutes resize-image-routes
  (GET "/:height/:width" [height width]
    (let [image-file "madagascar-morondava.jpeg"
          height (parse-int height)
          width (parse-int width)]
      (-> (resize-image image-file height width)
          (response/response)
          (response/content-type "image/jpeg"))))

  (route/not-found "Not Found"))

(def app
  (wrap-defaults resize-image-routes site-defaults))
