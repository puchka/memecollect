(ns memecollect.views.layout
  (:use [hiccup.page :only (html5 include-css include-js)])
  (:require [cemerick.friend :as friend]
            [hiccup.element :as e :only (link-to)]
            [memecollect.misc :as misc]))

(defn application [req title & content]
  (html5 {:lang "en"}
         [:head
          [:title title]
          (include-css "//stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css")
          (include-css "/css/design.css")]

         [:body
          [:nav {:class "navbar"}
           [:a {:class "navbar-brand"
                :href "/"}
            [:img {:src "/images/memecollect-logo.png"
                   :alt "Memecollect logo"
                   :width "64" :height "64"
                   }]
            ]
           (if (friend/identity req)
             
             [:p (e/link-to "/logout" "Click here to log out") "."]
             
             )
           ]
          [:div {:class "container"} content ]
          (include-js "/js/app.js")]))
