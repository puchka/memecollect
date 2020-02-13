(ns memecollect.core
  (:use ring.adapter.jetty hiccup.core))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (html [:span {:class "foo"} "bar"])})

(defn -main
  []
  (run-jetty handler {:port 3000}))
