(ns memecollect.util.sendmail
  (:require [postal.core :as post]
            [digest]
            [clojure.string :as cstr]
            [memecollect.data.persistence :as pers]))

(def env_vars {:base_url "MEMECOLLECT_BASE_URL"
               :hostname "MEMECOLLECT_SMTP_HOSTNAME"
               :port "MEMECOLLECT_SMTP_PORT"
               :username "MEMECOLLECT_SMTP_USERNAME"
               :password "MEMECOLLECT_SMTP_PASSWORD"
               })

(def SMTP_conf (reduce (fn [p [k v]]
                         (into p {k (System/getenv v)}))
                       {}
                       env_vars))

(defn assert_conf [conf env_vars user-name]
  (when
      (some #(cstr/blank? (second %)) conf)
    (println (str (cstr/join ", " (map #((first %) env_vars) (filter #(cstr/blank? (second %)) conf))) " environment variables were not set."))
    (swap! pers/email-sending-errors conj (list :email-activation-code user-name))))

(defn send-activation-code
  [user-name user-email activation-code]
  (assert_conf SMTP_conf env_vars user-name)
  (let [send-result (post/send-message {:host (:hostname SMTP_conf)
                                        :port (Integer/parseInt (:port SMTP_conf))
                                        :user (:username SMTP_conf)
                                        :pass (:password SMTP_conf)}
                                       {:from "memecollect.com@gmail.com"
                                        :to user-email
                                        :subject (str "hi " user-name "! Here is your activation code for memecollect.com")
                                        :body (str "Hi " user-name ",\n"
                                                   "Please click on the following link to activate your account on memecollect.com "
                                                   (:base_url SMTP_conf) "/activate-account?activation-code=" activation-code
                                                   "&q=" (digest/md5 (str user-name "/" user-email)) "\n\n"
                                                   "Have fun!\n"
                                                   "Marius"
                                                   )})]
    ;; TODO: Add logging when sending email and detect SMTP errors
    (when (not (= :SUCCESS (-> send-result :error)))
      (swap! pers/email-sending-errors conj (list :email-activation-code user-name (-> send-result :error))))))
