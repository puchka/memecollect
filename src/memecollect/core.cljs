(ns memecollect.core
  (:require [reagent.core :as r]))

(r/render
  [:h1 "Hello from Reagent!"]
  (.getElementById js/document "content"))
