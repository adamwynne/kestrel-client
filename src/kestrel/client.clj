(ns kestrel.client
  (:use
   [clojure.contrib.def :only [defn-memo]])
  (:import
   (net.spy.memcached MemcachedClient)
   (java.net InetSocketAddress)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- create-client
  "Creates a memcached client from the supplied host and port number"
  [& {:keys [host port]
      :or {host "localhost" port 22133}}]
  
  (MemcachedClient. (list (InetSocketAddress. host port))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn-memo default-client
  "Caches a connecton to the kestrel server for future use"
  [& {:keys [host port]
      :or {host "localhost" port 22133}}]
  
  (create-client host port))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn set-item
  "Adds an item to a specific queue"
  [queue-name data & {:keys [timeout] :or {timeout 0}}]
  
  (.set (default-client) queue-name timeout data))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- decorate-queue-name
  "Takes in the options and makes the queue name string"
  [queue-name timeout mode0 mode1]
  
  (str queue-name
       (if timeout (str "/t=" timeout))
       (if mode0 (str "/" (name mode0)))
       (if mode1 (str "/" (name mode1)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-item
  "Retrieves an item from a specific queue"
  [queue-name & {:keys [timeout mode0 mode1]}]
  
  (.get (default-client) (decorate-queue-name queue-name timeout mode0 mode1)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn delete-queue
  "removes a queue from kestrel, dropping items in it and removing the journal files"
  [queue-name]

  (.delete (default-client) queue-name))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn flush-queue
  "drops items in a queue, leaving it empty"
  [queue-name]

 ;FIXME: do the goods
  )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn flush-all
  "flushes all the queues"
  []

  (.flush (default-client)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-version
  "gets the kestrel version"
  []

  (into '{} (.getVersions (default-client))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-stats
  "gets the stats for the queues"
  []

  (let [stats (.getStats (default-client))]
    (into '{} (for [[k v] stats] [k (into '{} v)]))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;