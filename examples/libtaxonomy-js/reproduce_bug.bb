#!/usr/bin/env bb

(ns reproduce-bug
  (:require [babashka.fs :as fs]
            [babashka.process :as process]
            [clojure.string :as str]))

(def output-root "public/js")

(def problematic-target-file (fs/path
                              output-root
                              "libtaxonomy/my_component.jsx"))

(defn clean []
  {:post [(not (fs/exists? problematic-target-file))]}
  (fs/delete-tree output-root))

(defn get-compiled-file-set []
  (into #{} (map str) (file-seq output-root)))

(defn compile-using-watch []
  (let [proc (process/process {:err :inherit} "npx squint watch --repl true")]
    (Thread/sleep 1000)
    (process/destroy-tree proc)
    (assert (fs/exists? problematic-target-file))))

(defn parse-globalThis-assignment [s]
  (when-let [[_ mangled-namespace] (re-matches #"^globalThis\.(\S+)\s+=.*$" s)]
    mangled-namespace))

(defn parent-of? [a b]
  (and (str/starts-with? b a)
       (< (count a) (count b))))

(defn list-assignment-order-problems [globalThis-assignments]
  (for [[i a :as x] globalThis-assignments
        [j b :as y] globalThis-assignments
        :when (< i j)
        :when (parent-of? b a)]
    [x y]))

(defn report-result []
  (let [src (-> problematic-target-file
                fs/file
                slurp)
        globalThis-assignments (->> src
                                    str/split-lines
                                    (keep-indexed
                                     (fn [i x]
                                       (when-let [result (parse-globalThis-assignment x)]
                                         [(inc i) result]))))
        problems (list-assignment-order-problems globalThis-assignments)]
    (println "Compiled output:\n")
    (println src)
    (println "globalThis namespace assignment order:")
    (doseq [[line mangled-namespace] globalThis-assignments]
      (println (format "* Line %d: %s" line mangled-namespace)))
    (println)
    (if (seq problems)
      (do 
        (println (format "Detected %d problems:" (count problems)))
        (doseq [[[i x] [j y]] problems]
          (println (format "globalThis-assignment for '%s' on line %d should not precede '%s' on line %d"
                           x i y j))))
      (println "No problems found."))))

(defn reproduce-using-watch []
  (clean)
  (compile-using-watch)
  (report-result))

(defn reproduce-using-compile []
  (clean)
  (process/shell "npx squint compile --repl")
  (report-result))

(defn -main [& args]
  (case (first args)
    "reproduce-using-watch" (reproduce-using-watch)
    "reproduce-using-compile" (reproduce-using-compile)
    nil))

(apply -main *command-line-args*)
