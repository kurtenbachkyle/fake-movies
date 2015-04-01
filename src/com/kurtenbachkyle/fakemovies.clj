(ns com.kurtenbachkyle.fakemovies
  (:require [clojure.java.io :as io])
  (import com.atlascopco.hunspell.Hunspell))

(def speller (Hunspell. "/home/kyle/git/personal/fake-movies/resources/en_US-large.dic"
                        "/home/kyle/git/personal/fake-movies/resources/en_US-large.aff"))

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
                             (re-find #"\D" itm))
                      (map  (partial replace-word idx title-words) subwords) nil)))
                title-words))))


(defn output-alt-titles [title]
  (if-let [alt-titles (flatten  (title->altered-titles title))]
    (println-str title "\n" 
                 (apply str (interleave alt-titles (repeat "\n"))))))

(print  (output-alt-titles "park broker girls"))
(println  (.readLine  (io/reader  "resources/lots-of-reviews.list")))
