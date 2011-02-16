(ns rabbitcj.client
  (:import
    (com.rabbitmq.client
      Connection ConnectionFactory Channel
      MessageProperties QueueingConsumer AMQP)))

(def direct-exchange "direct")
(def fanout-exchange "fanout")

(defn #^Connection connect [opts]
    "Connect to an AMQP broker.
  Recognized options: :username, :password, :host, :port, :virtual-host"
  (let [conn-fac (ConnectionFactory.)]
    (doto conn-fac
      (.setUsername (:username opts))
      (.setPassword (:password opts))
      (.setVirtualHost (:virtual-host opts))
      (.setHost (:host opts))
      (.setPort (:port opts)))
    (.newConnection conn-fac)))

(defn #^Channel create-channel
  "Opens and returns a channel for the given connection."
  [#^Connection conn]
  (.createChannel conn))

;; update to 1.8.1 autodelete? args
(defn declare-exchange
  "Declares an exchange according to the given options."
  ([#^Channel channel exchange-name type]
     (.exchangeDeclare channel exchange-name type))
  ([#^Channel channel exchange-name exchange-type durable? autodelete? args]
     (.exchangeDeclare channel exchange-name exchange-type durable? autodelete? args)))

(defn declare-passive-queue [queue-name]
  "Declares a queue passively, ie check if it exists"
  (.queueDeclarePassive queue-name))

;; update to 1.8.1 exclusive? autodelete? args
(defn declare-queue [#^Channel channel queue-name durable? exclusive? autodelete? args]
  "Declares a queue according to the given options"
  (.queueDeclare channel queue-name durable? exclusive? autodelete? args))

(defn delete-exchange
  "Deletes an exchange."
  [#^Channel channel exchange-name]
  (.exchangeDelete channel exchange-name))

(defn delete-queue
  "Deletes a queue."
  [#^Channel channel queue-name]
  (.queueDelete channel queue-name))

(defn bind-queue
  "Binds a declared queue to a declared exchange."
  [#^Channel channel queue-name exchange-name routing-key]
  (.queueBind channel queue-name exchange-name routing-key))

(defn publish
  "Publish a message to an exchange."
  [#^Channel channel exchange routing-key #^String message]
   (.basicPublish channel exchange routing-key
     MessageProperties/MINIMAL_PERSISTENT_BASIC
     (.getBytes message "UTF-8")))

(defn consumer
  "Returns a consumer for the named queue."
  [#^Channel channel queue-name]
  (let [consumer (QueueingConsumer. channel)]
    (.basicConsume channel queue-name consumer)
    consumer))

(defn consume
  "Receives, acks, and returns a message for the given consumer. Returns nil
  if the blocking recieve times out."
  [#^Channel channel #^QueueingConsumer consumer timeout]
  (if-let [delivery (.nextDelivery consumer timeout)]
    (let [message (String. #^"[B" (.getBody delivery) "UTF-8")]
      (.basicAck channel (.. delivery getEnvelope getDeliveryTag) false)
      message)))

(defn message-count
  "Returns the number of messages in the queue."
  [#^Channel channel queue-name]
  (.getMessageCount (.queueDeclare channel queue-name)))

(defn channel-open?
  "Check if a channel is open"
  [#^Channel channel]
  (.isOpen channel))
