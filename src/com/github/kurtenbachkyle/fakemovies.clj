(ns com.github.kurtenbachkyle.fakemovies
  (:require [clojure.java.io :as io])
  (import com.atlascopco.hunspell.Hunspell))

(def speller (Hunspell. "resources/en_US.dic"
                        "resources/en_US.aff"))

(def boring-words {:the "the" :and "and"})
(defn word->subwords[word]
  (map (partial reduce str) 
       (map-indexed 
         (fn [idx itm] 
           (keep-indexed #(if (not= %1 idx) %2) word)) word)))

(defn word->real-subwords [word]
  (filter #(. speller spell (str  %)) (word->subwords word)))

(defn replace-word [i old-seq new-word]
  (apply str  (interleave (conj (repeat " ") "") (assoc (vec old-seq) i new-word))))

(defn title->altered-titles [word]
  (let [title-words (re-seq #"[a-zA-Z0-9-']+" word)]
      (filter identity 
              (map-indexed 
                (fn [idx itm]
                  (if-let [subwords (seq (word->real-subwords itm))]
                    (if (and (>  (count itm) 2)
                             (re-find #"\D" itm)
                             (not (contains? boring-words (keyword  (.toLowerCase itm)))))
                      (map  (partial replace-word idx title-words) subwords) nil)))
                title-words))))


(defn output-alt-titles [title]
  (if-let [alt-titles (seq  (flatten  (title->altered-titles title)))]
    (println-str title "\n\t" 
                 (apply str (interleave alt-titles (repeat "\n\t"))))))

(defn process-word-file [input output]
  (let [reader (io/reader input)] 
  (loop [title (.readLine  reader)
         result ""]
    (if-not title 
      (spit output result)
      (recur (.readLine reader) 
             (if-let [new-titles (output-alt-titles title)]
               (str result new-titles)
               result))))))

(defn get-alt-movie-titles [] 
  (process-word-file "resources/popular-movies.list" "resources/my-movies.txt"))
