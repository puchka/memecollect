(ns memecollect.util.properties)


;; Temporarily set Java System properties
(defmacro with-properties [property-map & body]
  `(let [pm# ~property-map
         props# (into {} (for [[k# v#] pm#]
                           [k# (System/getProperty k#)]))]
     (doseq [k# (keys pm#)]
       (System/setProperty k# (get pm# k#)))
     (try
       ~@body
       (finally
         (doseq [k# (keys pm#)]
           (if-not (get props# k#)
             (System/clearProperty k#)
             (System/setProperty k# (get props# k#))))))))
