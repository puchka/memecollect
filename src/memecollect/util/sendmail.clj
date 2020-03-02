(ns memecollect.util.sendmail
  (:require [postal.core :as post]
            [digest]))

(def base_url (System/getenv "MEMECOLLECT_BASE_URL"))
(def SMTP_hostname (System/getenv "MEMECOLLECT_SMTP_HOSTNAME"))
(def SMTP_username (System/getenv "MEMECOLLECT_SMTP_USERNAME"))
(def SMTP_password (System/getenv "MEMECOLLECT_SMTP_PASSWORD"))

(defn send-activation-code
  [user-name user-email activation-code]
  (post/send-message {:host SMTP_hostname
                      :user SMTP_username
                      :pass SMTP_password}
                     {:from "memecollect.com@gmail.com"
                      :to user-email
                      :subject (str "hi " user-name "! Here is your activation code for memecollect.com")
                      :body (str "Hi " user-name ",\n"
                                 "Please click on the following link to activate your account on memecollect.com "
                                 base_url "/activate-account?activation-code=" activation-code
                                 "&q=" (digest/md5 (str user-name "/" user-email)) "\n\n"
                                 "Have fun!\n"
                                 "Marius"
                                 )})  
  )
