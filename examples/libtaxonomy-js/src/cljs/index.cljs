(ns index
  (:require [libtaxonomy.my-component :as MyComponent]
            ["react-dom/client" :refer [createRoot]]))

(def root (createRoot (js/document.getElementById "app")))
#_(.render root #jsx [MyComponent/MyComponent])
(.render root #jsx [:div
                    [:h1 "My Component"]
                    ;;[MyComponent/MyComponent]
                    [:h1 "JobTech experiments"]])
