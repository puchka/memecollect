(ns memecollect.views.contents
  (:use [hiccup.form]
        [hiccup.element :only (link-to)]))

(defn index []
  [:div {:id "content"}
   [:h1 {:class "text-success"} "Welcome to memecollect!"]])

(defn not-found []
  [:div
   [:h1 {:class "info-warning"} "Page Not Found"]
   [:p "The requested url doesn't refer to an existing page. "]
   (link-to {:class "btn btn-primary"} "/" "Take me to Home")])
