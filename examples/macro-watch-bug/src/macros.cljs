(ns macros)

(defmacro greet [name]
  `(str "Hello, " ~name "!"))
