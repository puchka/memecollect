(ns memecollect.core-test
  (:require [clojure.test :refer :all]
            [memecollect.core :refer :all]
            [memecollect.util.sendmail :refer :all]
            [memecollect.data.persistence :as pers]
            [memecollect.util.properties :as prop])
  (:import (de.saly.javamail.mock2 MockMailbox)
           (javax.mail MessagingException
                       Session
                       Store
                       Folder
                       Transport)))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))

(deftest send-activation-code-test
  (testing "Error is logged when SMTP server is not provided"
    (prop/with-properties {"mail.transport.protocol.rfc822" "mock_smtp"}
      (is (thrown? MessagingException
           (let [mb (. MockMailbox get "mariusrabenarivo@gmail.com")
                 session (. Session getDefaultInstance (System/getProperties))
                 store (. session getStore "mock_pop3")]
             (-> mb (.getInbox) (.setSimulateError true))
             (with-redefs [pers/email-sending-errors (atom '())]
               (send-activation-code "marius" "mariusrabenarivo@gmail.com" 12356)
               (. store connect "mariusrabenarivo@gmail.com" nil)
               (let [inbox (.  store getFolder "INBOX")]
                 (. inbox open (. Folder READ_ONLY)))
               (is (not (= :SUCESS (:error (first @pers/email-sending-errors))))))
             ))))))
