(ns memecollect.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [clojure.string :as str])
  (:import goog.history.Html5History))

(defn check-nil-then-predicate
  "Check if the value is nil, then apply the predicate"
  [value predicate]
  (if (nil? value)
    false
    (predicate value)))

(defn valid-email?
  [email]
  (check-nil-then-predicate email (fn [arg] (boolean (first (re-seq #"\w+@\w+\.\w+" arg))))))

(defn eight-or-more-characters?
  [word]
  (check-nil-then-predicate word (fn [arg] (> (count arg) 7))))

(defn has-special-character?
  [word]
  (check-nil-then-predicate word (fn [arg] (boolean (first (re-seq #"\W+" arg))))))

(defn has-number?
  [word]
  (check-nil-then-predicate word (fn [arg] (boolean (re-seq #"\d+" arg)))))

(defn prompt-message
  "A prompt that will animate to help the user with a given input"
  [message]
  [:div {:class "my-messages"}
   [:div {:class "prompt message-animation"} [:p message]]])

(defn input-element
  "An input element which updates its value on change, focus and blur"
  [id name type value in-focus]
  [:input {:id id
           :name name
           :class "form-control"
           :type type
           :required ""
           :value @value
           :on-change #(reset! value (-> % .-target .-value))
           :on-focus #(swap! in-focus not)
           :on-blur (fn [arg] (if (nil? @value) (reset! value ""))(swap! in-focus not))
           }])

(defn input-and-prompt
  "Creates an input box and a prompt box that appears above the input when the input comes into focus. Also throws in a little required message"
  [label-value input-name input-type input-element-arg prompt-element required?]
  (let [input-focus (atom false)]
    (fn []
      [:div
       [:label {:for input-name} label-value]
       (if @input-focus prompt-element [:div])
       [input-element input-name input-name input-type input-element-arg input-focus]
       (if (and required? (= "" @input-element-arg))
         [:div {:id "validation-message"
                :class ["alert" "alert-danger"]
                :role "alert"} "Field is required!"]
         [:div])])))

(defn email-validation
  [email-address-atom]
  (if (or (clojure.string/blank? @email-address-atom) (valid-email? @email-address-atom))
    [:div]
    [:div {:class ["alert" "alert-danger"]
           :role "alert"} "Invalid email."]))

(defn email-form [email-address-atom]
  (let [email email-address-atom]
    (fn []
      [:div
       [(input-and-prompt "Email"
                          "email"
                          "email"
                          email-address-atom
                          (prompt-message "What's your email address?")
                          true)]
       [email-validation email]])))

(defn username-form [name-atom]
  (input-and-prompt "Username"
                    "username"
                    "text"
                    name-atom
                    (prompt-message "What's your name?")
                    true))

(defn password-requirements
  "A list to describe which password requirements have been met so far"
  [password requirements]
  [:div
   [:ul (->> requirements
             (filter (fn [req] (not ((:check-fn req) @password))))
             (doall)
             (map (fn [req] ^{:key req} [:li (:message req)])))]])

(defn password-form [password]
  (let [password-type-atom (atom "password")]
    (fn []
      [:div [(input-and-prompt "Password"
                                "password"
                                @password-type-atom
                                password
                                (prompt-message "What's your password?")
                                true)]
       [password-requirements password [{:message "8 or more characters" :check-fn eight-or-more-characters?}
                                        {:message "At least one special character" :check-fn has-special-character?}
                                        {:message "At least one number" :check-fn has-number?}]]])))

(defn confirm-password-form [password]
  (fn []
    (input-and-prompt "Confirm password"
                        "confirm"
                        "password"
                        password
                        (prompt-message "Confirm your password")
                        true)
     ))

(defn wrap-as-element-in-form
  [element]
  [:div {:class "row form-group"}
   element])

(defn home-page []
  [:h1 {:class "text-success"} "Welcome to memecollect!"])

(defn login-form []
  [:div {:class "row"}
   [:div {:class "columns small-12"}
    [:h3 "Login"]
    [:div {:class "row"}
     [:form {:method "POST" :action "login" :class "columns small-4"}
      [:div "Username" [:input {:type "text" :name "username"}]]
      [:div "Password" [:input {:type "password" :name "password"}]]
      [:div [:input {:type "submit" :class "button" :value "Login"}]]]]]])

(defn user-page []
  [:p (str "Welcome to " (session/get :current-user) "'s meme collection :)")])

(defn strong-password?
  [password]
  (and
   (eight-or-more-characters? password)
   (has-special-character? password)
   (has-number? password)))

(defn valid-form?
  [email name password confirm]
  (and (valid-email? @email) (not (str/blank? @name))
       (strong-password? @password) (= @password @confirm)))

(defn subscription-page []
  (let [email-address (atom nil)
        name (atom nil)
        password (atom nil)
        confirm (atom nil)]
    (fn []
      [:div {:class "signup-wrapper"}
       [:h2 "Create an account"]
       [:form {:method "POST" :action "signup"}
        (wrap-as-element-in-form [email-form email-address])
        (wrap-as-element-in-form [username-form name])
        (wrap-as-element-in-form [password-form password])
        (wrap-as-element-in-form [confirm-password-form confirm])
        (wrap-as-element-in-form [:div "Make you an admin? " [:input {:type "checkbox" :name "admin"}]])
        (wrap-as-element-in-form [:input {
                                          :type "submit"
                                          :class "button"
                                          :value "Sign up"
                                          :disabled (boolean (not (valid-form? email-address name password confirm)))
                                          }])
        ]
       [:div (if (valid-email? @email-address) [:div] [:div {:class ["alert" "alert-warning"] } "Enter a valid email address"])
        (if (not (str/blank? @name)) [:div] [:div {:class ["alert" "alert-warning"] }"Enter an username"])
        (if (strong-password? @password) [:div] [:div {:class ["alert" "alert-warning"] }"Enter a strong password"])
        (if (= @password @confirm) [:div] [:div {:class ["alert" "alert-warning"] }"Enter the same password and confirmation password"])]
       ]
      )))

(defn account-activation-page []
  [:h1 "Account Activation"])

(defn current-page []
  [:div [(session/get :current-page)]])

(secretary/defroute "/" []
  (session/put! :current-page home-page))

(secretary/defroute "/subscribe" []
  (session/put! :current-page subscription-page))

(secretary/defroute "/login" []
  (session/put! :current-page login-form))

(secretary/defroute "/user/:user" [user]
  (session/put! :current-page user-page)
  (session/put! :current-user user))

(secretary/defroute "/activate-account" []
  (session/put! :current-page account-activation-page))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (Html5History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setUseFragment false)
    (.setPathPrefix "")
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn init! []
  (reagent/render-component [current-page] (.getElementById js/document "app")))

(hook-browser-navigation!)

(.addEventListener
  js/window
  "DOMContentLoaded"
  (init!))
