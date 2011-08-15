(ns BioClojure.core
  (:use [clojure.pprint])
  (:import (org.apache.commons.lang StringUtils)))


(defn transform-entry
  "Transform a meta string into a map of metadata about a DNA entry."
  [entry]
  (let [meta-props (seq (-> (subs entry 1) ;; take off the leading ">"
                            (.split " ")))
        name (first meta-props)
        props (drop 1 meta-props)]
    (assoc (into {} (map #(vec (.split % "=")) props)) "name" name)))

(defn read-fasta-file
  "Read a FASTA file, transforming it into a map with :meta and :dna keys."
  [filename]
  (let [text (.trim (slurp filename))
        entries (-> text (.replaceAll "\r" "") (.split "\n\n") seq)]
    (map #(let [[m dna] (.split % "\n")]
            {:meta (transform-entry m) :dna dna})
         entries)))

(defn read-motif-file
  "Read a motif file, transforming it into a map with :meta and :dnamotif keys."  
  [filename]
  (.split (.trim (slurp filename)) "\n"))

;;Create a random nucleotide, from a c g t.  Used to generate motifs randomly. 
;;DO NOT Call this method by itself, it will go for infinity. 
(defn rand-nuc "Returns a random nucleotide" [] (repeatedly #(get ["a" "c" "g" "t"] (rand-int 4))))

;;Creates a DNA motif of random length
;;user=> (rand-motif-str 4)
;;("a" "c" "c" "c")
(defn rand-motif-str "Returns a random motif" [l] (take l (rand-nuc)))

(defn read-and-print
  "Read a FASTA file and print it."
  [filename]
  (pprint (read-fasta-file filename)))

;; From the project directory:
;; lein repl
;; (use 'BioClojure.core)
;; (read-and-print "data/sample.fa")

;; 1 Identify motifs (M1 - Mx) present in DNA and their order
;; Confirm initiator motif and terminator motif and positions
;; Confirm proper insertion into two restriction sites.
;; Flag any incorrect assemblies and sequences with N positions
;; Report total number of minimotifs in clone
;; Generate a graph of overall structure, show linker regions that
;; glue motifs together and alignm
;; of motifs to sequences with labels.
;; 7. Report any 1 nucleotide mutations in DNA

