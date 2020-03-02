(ns memecollect.users
  (:require
   [cemerick.friend.credentials :as creds]
   [digest]))

(derive ::admin ::user)

(def MIN_ACTIVATION_CODE 10000)
(def MAX_ACTIVATION_CODE 99999)
(defn- activation-code
  []
  (+ (rand-int (- MAX_ACTIVATION_CODE MIN_ACTIVATION_CODE)) MIN_ACTIVATION_CODE));

(def active-status ::active)

(defn create-user
  [{:keys [username email password admin] :as user-data}]
  (-> (dissoc user-data :admin)
      (assoc :identity username
             :email email
             :password (creds/hash-bcrypt password)
             :roles (into #{::user} (when admin [::admin]))
             :activation-code (activation-code)
             :user-identity-hash (digest/md5 (str username "/" email))
             :status ::inactive
             )))
