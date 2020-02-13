(defproject memecollect "0.1.0-SNAPSHOT"
  :description "Meme collection app"
  :url "http://www.memecollect.com"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [hiccup "1.0.5"]]
  :repl-options {:init-ns memecollect.core}
  :main memecollect.core/-main)
