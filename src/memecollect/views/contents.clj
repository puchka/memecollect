(ns memecollect.views.contents
  (:require [hiccup.form]
            [hiccup.element :as e :only (link-to)]
            [cemerick.friend :as friend]
            [memecollect.misc :as misc]
            [memecollect.users :as users]))

(defn index [req]
  [:div
   [:div {:id "app"}]
   [:p (if-let [identity (friend/identity req)]
         (apply str "Logged in, with these roles: "
                (-> identity friend/current-authentication :roles))
         "anonymous user")]
   [:ul
    (if-let [identity (friend/identity req)]
      [:li (e/link-to (misc/context-uri req
                                        (str "user/" (-> identity friend/current-authentication :username)))
                      "My meme collection")])
    (if-let [identity (friend/identity req)]
      (when (-> identity friend/current-authentication :roles (contains? ::users/admin))
        [:li (e/link-to (misc/context-uri req "admin") "Admin dashboard")]))
    [:li (e/link-to (misc/context-uri req "requires-authentication")
                    "Requires any authentication, no specific role requirement")]
    (if (friend/identity req)
      [:div
       [:h3 "Logging out"]
       [:p (e/link-to (misc/context-uri req "logout") "Click here to log out") "."]]
      [:div
       [:h3 "Logging in"]
       [:p (e/link-to (misc/context-uri req "login") "Click here to log in") "."]]
      )
    (if (not (nil? (:flash req)))
      [:div (:flash req)]
      [:div])]])

(defn not-found []
  [:div
   [:h1 {:class "info-warning"} "Page Not Found"]
   [:p "The requested url doesn't refer to an existing page. "]
   (e/link-to {:class "btn btn-primary"} "/" "Take me to Home")])


(defn subscribe []
  [:div#app])

(defn login []
  [:div#app])

(defn user
  [req]
  [:div#app])

(defn activate-account
  [activation-status]
  [:div
   [:div#app]
   (if activation-status
     [:p "Account activation successful!"]
     [:p "An error occured during the activation of your account :("])
   ]
  )
