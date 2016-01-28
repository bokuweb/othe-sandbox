(ns othe.core ;名前空間を定義
  (:use
    othe.view
    othe.model))

(defn on-command ;Viewからのコマンド通知を処理するハンドラ
  [cmdline]
  (let [cmd (first cmdline)
        pos(second cmdline)]
    (cond
      (= cmd :move) (play-movepos)
      (= cmd :ext) (System/exit 0)
      :else nul)))

(defn -main ;エントリポイント
  [&args]
  (init-iew on-command)
  (init-game on-state-changed)
  (start-ui))
