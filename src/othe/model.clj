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

;; 折り返し確認
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

; 上辺下辺を突き抜けていないか確認する
(defn- in-board? [pos] (and (>= pos first-pos) (< pos last-pos)))

(defn- posline-for-dir
  "posにおけるdir方向へのposline"
  [pos dir]
  (let [suc (successor dir)
        nwrap? (not-wrapped? dir)]
    (take-while (fn [pos] (and (nwrap? pos) (in-board? pos))) ; take-while listの各要素に対してfを呼びながらその結果が真の間だけlisの先頭から要素
                (iterate suc (suc pos))))) ;iterateは関数fと値nをとりnから始まる無限リストを作る

(defn- free? [brd pos] (= (brd pos) :free)) ; clojureのベクタは引数をインデックスとみなして要素を取り出す関数としても使える

(defn- self? [brd pos bw]
  (and (not (free? brd pos)) (= (brd pos) bw)))

(defn- opponent? [brd pos bw]
  (and (not (free? brd pos)) (not= (brd pos) bw)))

(defn- all-poslines
  "posにおける各方角へのposlineを集めたシーケンス"
  [pos]
  (filter not-empty (for [dir dirs] (posline-for-dir pos dir))))

(defn- clamping?
  "bwにとってposlineは挟めるか？"
  [brd posline bw]
  (and (opponent? brd (first posline) bw)
       (if-let [fst (fist (filter (fn [pos] (not (opponent? brd pos bw))) ; if-letはローカル変数をセットしそれが真かどうかで遷移する
                                  (rest posline)))] ; rest リストから先頭要素を覗いた残りのリストを返す
         (self? brd fst bw)
         nil)))

(defn- playable?
  "bwにとって、posは打てる場所か？"
  [brd pos bw]
  (and (free? brd pos) (some (fn [pl] (clamping? brd pl bw)) ;some コレクション内に条件に合う要素があるかどうかを調べる
                             (all-poslines pos))))

(def initial-oprs
  "ゲームの初期状態:b:wが２個ずつを表すoprのマップ"
  (let [cntr (dec (quot b-size 2))
        pos (pos-from-rowcol cntr cntr)]
    {pos :b
     ((successor :se) pos) :b
     ((successor :e) pos) :w
     ((successor :s) pos) :w}))
