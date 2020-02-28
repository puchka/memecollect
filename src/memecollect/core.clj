(ns memecollect.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as resp]
            [ring.middleware.resource :as res]
            [compojure.core :refer [defroutes GET POST ANY]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])
            [clojure.string :as str]
            [hiccup.page :as h]
            [memecollect.misc :as misc]
            [memecollect.views.layout :as layout]
            [memecollect.views.contents :as contents]
            [memecollect.users :as users]
            [memecollect.data.persistence :as pers]))


(defn- create-user
  [{:keys [username password admin] :as user-data}]
  (-> (dissoc user-data :admin)
      (assoc :identity username
             :password (creds/hash-bcrypt password)
             :roles (into #{::users/user} (when admin [::users/admin])))))

(defroutes routes
  (GET "/" req (layout/application "Home" (contents/index req)))
  (GET "/subscribe" [] (layout/application "Subscription" (contents/subscribe)))
  (GET "/login" req
    (layout/application "Login" (contents/login)))
  (GET "/logout" req
    (friend/logout* (resp/redirect (str (:context req) "/"))))
  (POST "/signup" {{:keys [username password confirm] :as params} :params :as req}
        (if (and (not-any? str/blank? [username password confirm])
                 (= password confirm))
          (let [user (create-user (select-keys params [:username :password :admin]))]
            (swap! pers/users assoc username user)
            (friend/merge-authentication
              (resp/redirect (misc/context-uri req (str "user/" username)))
              user))
          (assoc (resp/redirect (str (:context req) "/")) :flash "passwords don't match!")))
  (GET "/requires-authentication" req
    (friend/authenticated "Thanks for authenticating!"))
  (GET "/user/:user" req
       (if (get @pers/users (:user (:params req)))
         (friend/authorize #{::users/user}
                           (layout/application (str (:user (:params req)) "'s list") (contents/user req)))
         (route/not-found (layout/application "User Not Found" (contents/not-found)))))
  (GET "/admin" req
    (friend/authorize #{::users/admin} "You're an admin!"))
  (route/resources "/")
  (ANY "*" [] (route/not-found (layout/application "Page Not Found" (contents/not-found)))))

(def page (->
            (friend/authenticate
              routes
              {:allow-anon? true
               :login-uri "/login"
               :default-landing-uri "/"
               :unauthorized-handler #(-> (h/html5 [:h2 "You do not have sufficient privileges to access " (:uri %)])
                                        resp/response
                                        (resp/status 401))
               :credential-fn #(creds/bcrypt-credential-fn @pers/users %)
               :workflows [(workflows/interactive-form)]})
            (handler/site)
            (res/wrap-resource "public")))

(defn -main
  []
  (jetty/run-jetty page {:port 3000}))
