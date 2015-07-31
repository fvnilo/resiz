(ns resiz.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [image-resizer.resize :refer :all]
            [image-resizer.format :as format]
            [image-resizer.scale-methods :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as response]))

(defn get-full-path [path]
  (-> path (io/resource) (.getPath)))

(defn compute-ratio [width height]
  (/ width height))

(defn compute-image-file-ratio [path]
  (with-open [stream (java.io.FileInputStream. (get-full-path path))]
    (let [image (javax.imageio.ImageIO/read stream)]
      (compute-ratio (.getWidth image) (.getHeight image)))))

(defn ratio-valid? [path w h]
  (let [width (read-string w)
        height (read-string h)]

    (= (compute-ratio width height) (compute-image-file-ratio path))))

(defn number-string? [number-string]
  (integer? (read-string number-string)))

(defn dimensions-valid? [& dimensions]
  (every? number-string? dimensions))

(defn image-exists? [path]
  (not (nil? (io/resource path))))

(defn valid? [path w h]
  (and (dimensions-valid? w h) (ratio-valid? path w h)))

(defn resize-image [path height width]
  (let [resizer (resize-fn height width speed)
        img-file (-> path (get-full-path) (io/file))]
        (format/as-stream (resizer img-file) "jpg")))

(defn resize-handler [w h path]
  (if (not (image-exists? path))
    (response/status (response/response "image does not exists") 404)
    (if (not (valid? path w h))
      (response/status (response/response "invalid parameters") 400)
      (let [width   (Integer. w)
            height (Integer. h)
            image-file path]

        (with-open [image-stream (resize-image image-file height width)]
          (-> image-stream
              (response/response)
              (response/content-type "image/jpeg")))))))

(defroutes resize-image-routes
   (GET "/:width/:height/*" request
    (let [params (request :params)
          width (params :width)
          height (params :height)
          image-path (params :*)]

          (resize-handler width height image-path)))
   (route/not-found "Not Found"))

(def app
  (wrap-defaults resize-image-routes site-defaults))
