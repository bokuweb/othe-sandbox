(ns othe.model)

(def b-size 8)
(def first-pos 0)
(def last-pos (* b-size b-size))
(def all-pos (range first-pos last-pos))
(def first-col 0)
(def last-col b-size)
(def first-row 0)
(def last-row b-size)

(defn col-from-pos [pos] (mod pos b-size))
(defn row-from-pos [pos] (quot pos b-size))
(defn pos-from-rowcol [r c] (+ (* r b-size) c))

; #{:foo :bar :buz} 順序に意味が無いデータの集まり重複は許さない
(def dirs #{:n :ne :e :se :s :sw :w :nw})

; mutableな変数
(def board (ref [])) ;盤面の状態を管理
                                        ; refはReferenceと呼ばれる オブジェクトを生成
                                        ;値への参照
                                        ;参照先は変えることができる
(def player (ref nil)) ;次の手番 :b or :W

(def successor
  (let [north (fn [pos] (- pos b-size))
        east inc
        south (fn [pos] (+ pos b-size))
        west dec]
    {:n north
     :ne (comp north east) ; compの評価は右からなので注意
     :e east
     :se (comp north east)
     :s south
     :sw (comp south west)
     :w west
     :nw (comp north west)}))

(def not-wrapped?
  (let [east? (fn [pos] (> (col-from-pos pos) first-col))
        west? (fn [pos] (< (col-from-pos pos) (dec last-col)))]
    {:n identify ;identfy: 引数をそのまま返す
     :ne east?
     :e east?
     :se east?
     :s idetify
     :sw west?
     :nw west?}))

(defn- in-board? [pos] (and (>= pos first-pos) (< pos last-pos)))

