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

;; (defn random-id []
;;   (str (java.util.UUID/randomUUID)))

;; (defn bootstrap-consumer
;;   "Declares an exchange and a queue, then binds them together."
;;   [channel exch exch-type queue id]
;;   (let [consumer (QueueingConsumer. channel)]
;;     (doto channel
;;       (.exchangeDeclare exch exch-type)
;;       (.queueDeclare queue)
;;       (.basicConsume queue consumer)
;;       (.queueBind queue exch id)) consumer))

;; (defn fan-out-using-keys []
;;   (with-open [connection (connect host)
;;               channel (.createChannel connection)]
;;     (let [queue-name (random-id)
;;           queue-name2 (random-id)
;;           id (random-id)
;;           exchange-name (random-id)
;;           consumer  (bootstrap-consumer channel exchange-name direct-exchange queue-name id)
;;           consumer2 (bootstrap-consumer channel exchange-name direct-exchange queue-name2 id)]
;;       (publish channel exchange-name id "hello")
;;       (str (consume consumer channel) "  " (consume consumer2 channel)))))

;; (defn fan-out-using-fanout-exchange []
;;   (with-open [connection (connect host)
;;               channel (.createChannel connection)]
;;     (let [queue-name (random-id)
;;           queue-name2 (random-id)
;;           id (random-id)
;;           exchange-name (random-id)
;;           consumer  (bootstrap-consumer channel exchange-name fanout-exchange queue-name "boz")
;;           consumer2 (bootstrap-consumer channel exchange-name fanout-exchange queue-name2 "biz")]
;;       (publish channel exchange-name "baz" "hello")
;;       (str (consume consumer channel) "  " (consume consumer2 channel)))))

;; (defn two-independent-queues []
;;   (with-open [connection (connect host)
;;               channel (.createChannel connection)]
;;     (let [queue-name (random-id)
;;           queue-name2 (random-id)
;;           id (random-id)
;;           exchange-name (random-id)
;;           exchange-name2 (random-id)
;;           consumer  (bootstrap-consumer channel exchange-name direct-exchange queue-name id)
;;           consumer2 (bootstrap-consumer channel exchange-name2 direct-exchange queue-name2 id)]
;;       (publish channel exchange-name id "hello")
;;       (publish channel exchange-name2 id "goodbye")
;;       (str (consume consumer channel) "  " (consume consumer2 channel)))))


;; (defn service-master []
;;   (react-to-commands (new-commands (fetch-commands master-username))))

;; (defn queues-view []
;;   (html [:p "There are " (count-feeds) " urls on the queue."]))

;;  [:get "/queues"]
;;       (response/response (queues-view))

;; (defvar bot-exchange
;;   "bot")

;; (defvar fetch-and-tweet-queue
;;   "fetch-and-tweet")

;; (defn declare-queues [clear]
;;   (with-open [conn (rmq/connect rabbitmq-params)]
;;     (with-open [chan (rmq/create-channel conn)]
;;       (rmq/declare-exchange chan bot-exchange rmq/direct-exchange true)
;;       (when clear
;;         (rmq/delete-queue chan fetch-and-tweet-queue))
;;       (rmq/declare-queue chan fetch-and-tweet-queue true)
;;       (rmq/bind-queue chan fetch-and-tweet-queue bot-exchange fetch-and-tweet-queue))))

;; (defvar rabbitmq-params
;;   {:username     "guest"
;;    :password     "guest"
;;    :host         "127.0.0.1"
;;    :port         5672
;;    :virtual-host "/"})

;; (deftest test-enqueue-feeds
;;   (bot/declare-queues true)
;;   (bot/enqueue-feeds)
;;   (is (= 105 (bot/count-feeds))))
