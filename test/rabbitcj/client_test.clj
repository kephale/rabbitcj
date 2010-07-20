(deftest rabbitcj.clinet-test
  (:use clojure.test)
(require [rabbitcj.client :as r])

(def conn-params
  {:username     "guest"
   :password     "guest"
   :host         "127.0.0.1"
   :port         5672
   :virtual-host "/"})

;; (with-open [conn (r/connect conn-params)]
;;   (with-open [chan (r/create-channel conn)]
;;     (r/declare-exchange chan "warren" r/direct-exchange true)
;;     (r/delete-queue chan "rabbits")
;;     (r/declare-queue chan "rabbits" true)
;;     (r/bind-queue chan "rabbits" "warren" "rabbits")
;;     (doseq [rabbit ["bob" "ted" "amy"]]
;;       (r/publish chan "warren" "rabbits" rabbit))
;;     (println "rabbits left:" (r/message-count chan "rabbits"))
;;     (let [consumer (r/consumer chan "rabbits")]
;;       (dotimes [i 4]
;;         (let [rabbit (r/consume chan consumer 1000)]
;;           (println (inc i) rabbit))))
;;     (println "rabbits left:" (r/message-count chan "rabbits"))))
