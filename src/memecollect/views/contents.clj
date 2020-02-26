(ns memecollect.views.contents
  (:use [hiccup.form]
        [hiccup.element :only (link-to)]))

(defn index []
  [:div {:id "app"}])

(defn not-found []
  [:div
   [:h1 {:class "info-warning"} "Page Not Found"]
   [:p "The requested url doesn't refer to an existing page. "]
   (link-to {:class "btn btn-primary"} "/" "Take me to Home")])


(defn subscribe []
  [:div#app])
