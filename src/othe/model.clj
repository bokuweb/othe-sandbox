(ns othe.model)

(def b-size 8)
(def first-pos 0)
(def last-pos (* b-size b-size))
(def all-pos (range first-pos last-pos))
(def first-col 0)
(def last-col b-size)
(def first-row 0)
(def last-row b-size)

(defun col-from-pos [pos] (mod pos b-size))
(defun row-from-pos [pos] (quot pos b-size))
(defun pos-from-rowcol [r c] (+ (* r b-size) c))

; #{:foo :bar :buz} 順序に意味が無いデータの集まり重複は許さない
(def dirs #{:n :ne :e :se :s :sw :w :nw})

; mutableな変数
(def board (ref [])) ;盤面の状態を管理
                                        ; refはReferenceと呼ばれる オブジェクトを生成
                                        ;値への参照
                                        ;参照先は変えることができる
(def player (ref nil)) ;次の手番 :b or :W
