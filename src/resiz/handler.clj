(ns resiz.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [image-resizer.resize :refer :all]
            [image-resizer.format :as format]
            [image-resizer.scale-methods :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as response]))

(defn number-string? [number-string]
  (integer? (read-string number-string)))

(defn valid? [& number-strings]
  (every? number-string? number-strings))

(defn resize-image [path height width]
  (let [resizer (resize-fn height width speed)
        img-file (-> path (io/resource) (.getPath) (io/file))]
        (format/as-stream (resizer img-file) "jpg")))

(defn resize-handler [h w]
    (if (not (valid? h w))
      (response/status (response/response "invalid parameters") 400)
      (let [height (Integer. h)
            width   (Integer. w)
            image-file "madagascar-morondava.jpeg"]

        (with-open [image-stream (resize-image image-file height width)]
          (-> image-stream
              (response/response)
              (response/content-type "image/jpeg"))))))    

(defroutes resize-image-routes
   (GET "/:height/:width" [height width] (resize-handler height width))
   (route/not-found "Not Found"))

(def app
  (wrap-defaults resize-image-routes site-defaults))
