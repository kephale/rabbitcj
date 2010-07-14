(ns rabbitcj.client
  (:import
    (com.rabbitmq.client
      Connection ConnectionFactory ConnectionParameters Channel
      MessageProperties QueueingConsumer)))

(def direct-exchange "direct")
(def fanout-exchange "fanout")

(defn #^Connection connect
  "Connect to an AMQP broker.
  Recognized options: :username, :password, :host, :port, :virtual-host"
  [opts]
  (let [params  (doto (ConnectionParameters.)
                  (.setUsername (:username opts))
                  (.setPassword (:password opts))
                  (.setVirtualHost (:virtual-host opts))
                  (.setRequestedHeartbeat 0))
        factory (ConnectionFactory. params)]
    (.newConnection factory #^String (:host opts) #^Integer (:port opts))))

(defn #^Channel create-channel
  "Opens and returns a channel for the given connection."
  [#^Connection conn]
  (.createChannel conn))

(defn declare-exchange
  "Declares an exchange according to the given options."
  [#^Channel channel exchange-name exchange-type durable?]
  (.exchangeDeclare channel exchange-name exchange-type durable?))

(defn declare-queue [#^Channel channel queue-name durable?]
  "Declares a queue according to the given options"
  (.queueDeclare channel queue-name durable?))

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
