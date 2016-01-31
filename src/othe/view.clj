(ns othe.view
  (:use
    othe.model
    [clojure.string :only (join)]))

(def code-a 97)
(def code-curly 123)

(def col-headers
  (take b-size
        (map (comp str char)
             (range code-a code-curly))))

(def col-header-str
  (str " " (join " " col-headers)))

(defn- st-str
  "ますの状態を表すも文字列"
  [st]
  (cond
    (= st :b) "x"
    (= st :w) "o"
    :else " "))


