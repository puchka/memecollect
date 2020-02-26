(ns memecollect.core
  (:require [ring.adapter.jetty :as jetty]
            [compojure.core :refer [defroutes GET ANY]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [memecollect.views.layout :as layout]
            [memecollect.views.contents :as contents]))

(defroutes routes
  (GET "/" [] (layout/application "Home" (contents/index)))
  (GET "/subscribe" [] (layout/application "Subscription" (contents/subscribe)))
  (route/resources "/")
  (ANY "*" [] (route/not-found (layout/application "Page Not Found" (contents/not-found)))))

(def application (handler/site routes))

(defn -main
  []
  (jetty/run-jetty application {:port 3000}))
