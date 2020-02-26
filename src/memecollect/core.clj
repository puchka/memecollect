(ns memecollect.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as resp]
            [cemerick.friend.credentials :refer (hash-bcrypt)]
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
            [memecollect.views.contents :as contents]))

(def users (atom {"friend" {:username "friend"
                            :password (hash-bcrypt "clojure")
                            :pin "1234" ;; only used by multi-factor
                            :roles #{::user}}
                  "friend-admin" {:username "friend-admin"
                                  :password (hash-bcrypt "clojure")
                                  :pin "1234" ;; only used by multi-factor
                                  :roles #{::admin}}}))

(derive ::admin ::user)

(defn- create-user
  [{:keys [username password admin] :as user-data}]
  (-> (dissoc user-data :admin)
      (assoc :identity username
             :password (creds/hash-bcrypt password)
             :roles (into #{::user} (when admin [::admin])))))

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
            (swap! users assoc username user)
            (friend/merge-authentication
              (resp/redirect (misc/context-uri req username))
              user))
          (assoc (resp/redirect (str (:context req) "/")) :flash "passwords don't match!")))
  (GET "/requires-authentication" req
    (friend/authenticated "Thanks for authenticating!"))
  (GET "/role-user" req
    (friend/authorize #{::user} "You're a user!"))
  (GET "/role-admin" req
    (friend/authorize #{::admin} "You're an admin!"))
  (route/resources "/")
  (ANY "*" [] (route/not-found (layout/application "Page Not Found" (contents/not-found)))))

(def page (handler/site
            (friend/authenticate
              routes
              {:allow-anon? true
               :login-uri "/login"
               :default-landing-uri "/"
               :unauthorized-handler #(-> (h/html5 [:h2 "You do not have sufficient privileges to access " (:uri %)])
                                        resp/response
                                        (resp/status 401))
               :credential-fn #(creds/bcrypt-credential-fn @users %)
               :workflows [(workflows/interactive-form)]})))

(defn -main
  []
  (jetty/run-jetty page {:port 3000}))
