(defproject memecollect "0.1.0-SNAPSHOT"
  :description "Meme collection app"
  :url "http://www.memecollect.com"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.11.132" :scope "provided"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [hiccup "1.0.5"]
                 [compojure "1.6.1"]
                 [cljsjs/react "17.0.2-0"]
                 [cljsjs/react-dom "17.0.2-0"]
                 [reagent "1.2.0"]
                 [reagent-utils "0.3.8"]
                 [clj-commons/secretary "1.2.4"]
                 [com.cemerick/friend "0.2.3"]
                 [duratom "0.5.1"]
                 [com.draines/postal "2.0.3"]
                 [digest "1.4.9"]]
  :repl-options {:init-ns memecollect.core}
  :main memecollect.core/-main
  :profiles {:test {:dependencies [[de.saly/javamail-mock2-fullmock "0.5-beta4"]]}}
  :plugins [[lein-cljsbuild "1.1.8"]
            [lein-figwheel "0.5.18"]]
  :cljsbuild {:builds [{:id "main"
                        :source-paths ["src"]
                        :figwheel true
                        :compiler {:main "memecollect.core"
                                   :output-to "resources/public/js/app.js"
                                   :output-dir "resources/public/js/out"
                                   :asset-path "/js/out"}}]}
  )
