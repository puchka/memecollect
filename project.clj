(defproject memecollect "0.1.0-SNAPSHOT"
  :description "Meme collection app"
  :url "http://www.memecollect.com"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.597" :scope "provided"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [hiccup "1.0.5"]
                 [compojure "1.6.1"]
                 [reagent "0.9.1"]
                 [reagent-utils "0.3.3"]
                 [clj-commons/secretary "1.2.4"]
                 [com.cemerick/friend "0.2.3"]
                 [duratom "0.5.1"]]
  :repl-options {:init-ns memecollect.core}
  :main memecollect.core/-main
  :plugins [[lein-cljsbuild "1.1.7"]]
  :cljsbuild {:builds [{:id "main"
                        :source-paths ["src"]
                        :compiler {:main "memecollect.core"
                                   :output-to "resources/public/js/app.js"
                                   :output-dir "resources/public/js/out"
                                   :asset-path "js/out"}}]}
  )
