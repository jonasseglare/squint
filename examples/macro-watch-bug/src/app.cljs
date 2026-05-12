(ns app
  (:require-macros [macros :refer [greet]]))

(js/console.log (greet "world"))
