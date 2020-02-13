(ns memecollect.views.layout
  (:use [hiccup.page :only (html5 include-css)]))

(defn application [title & content]
  (html5 {:lang "en"}
         [:head
          [:title title]
          (include-css "//stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css")

          [:body
           [:div {:class "container"} content ]]]))
