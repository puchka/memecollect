(ns memecollect.data.persistence
  (:require [duratom.core :as core]
            [clojure.java.io :as io])
  (:import (java.nio.file Files Paths LinkOption)))

(def data_dir_path (System/getenv "MEMECOLLECT_DATA_DIR"))

(when (not (Files/exists (Paths/get (new java.net.URI (str "file:///" data_dir_path)))
                         (into-array LinkOption [LinkOption/NOFOLLOW_LINKS])))
  (.mkdir (io/file data_dir_path)))

;; Define users duratom
(def users (core/duratom :local-file
                         :file-path (.getPath (io/file data_dir_path "users"))
                         :init {}))

(def email-sending-errors (core/duratom :local-file
                                       :file-path (.getPath (io/file data_dir_path "email-sending-errors"))
                                       :init '()))
