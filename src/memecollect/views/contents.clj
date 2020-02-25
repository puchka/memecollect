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

(defn labeled-radio [label]
  [:label (radio-button "user.gender" false label)
   (str label "    ")])

(defn subscribe []
  [:div {:class "well"}
   [:h1 "Create account"]
   [:form {:novalidate "" :role "form"}
    [:div {:class "form-group"}
     (label {:class "control-label"} "email" "Email")
     (email-field {:class "form-control" :placeholder "Email"} "user.email")]
    [:div {:class "form-group"}
     (label {:class "control-label"} "password" "Password")
     (password-field {:class "form-control" :placeholder "Password"} "user.password")]
    [:div {:class "form-group"}
     (label {:class "control-label"} "gender" "Gender")
     (reduce conj [:div {:class "btn-group"}] (map labeled-radio ["male" "female" "other"]))]
    [:div {:class "form-group"}
     [:label
      (check-box "user.remember-me") " Remember me"]]]
   [:pre "form = {{ user | json }}"]])
