(ns kestrel.test.client
  (:use
   [clojure.test]
   [kestrel.client]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmacro with-queue
  [queue-name & body]
  
  `(do (delete-queue ~queue-name)
       ~@body
       (delete-queue ~queue-name)))

(deftest test-create-client
  (is (= (type (#'kestrel.client/create-client))
         net.spy.memcached.MemcachedClient)))

(deftest test-delete
  (let [q-name "testq"
        q-val "test value"]
    (set-item q-name q-val)
    (delete-queue q-name)
    (is (nil? (get-item q-name)))))

(deftest test-queue
  (let [q-name "testq"]
    (with-queue q-name
      (doseq [i (range 10)] (set-item q-name i))
      (is (= (range 10) (repeatedly 10 #(java.lang.Integer/parseInt (get-item q-name))))))))

(deftest test-queue-name
  (is (= (#'kestrel.client/decorate-queue-name "myname" nil nil nil) "myname"))
  (is (= (#'kestrel.client/decorate-queue-name "myname" 0 nil nil) "myname/t=0"))
  (is (= (#'kestrel.client/decorate-queue-name "myname" 1000 nil nil) "myname/t=1000"))
  (is (= (#'kestrel.client/decorate-queue-name "myname" nil :open nil) "myname/open"))
  (is (= (#'kestrel.client/decorate-queue-name "myname" nil :close :open) "myname/close/open"))
  (is (= (#'kestrel.client/decorate-queue-name "myname" nil :close nil) "myname/close"))
  (is (= (#'kestrel.client/decorate-queue-name "myname" 1000 :abort nil) "myname/t=1000/abort")))

(deftest test-flush-queue
  (let [q-name "testq"]
    (with-queue q-name
      (set-item q-name 1)
      (set-item q-name 2)
      (flush-queue q-name)
      (is (nil? (get-item q-name))))))

(deftest test-set-get
  (let [q-name "testq"
        q-val "test value"]
    (with-queue q-name
      (set-item q-name q-val)
      (is (= (get-item q-name) q-val)))))

(deftest test-version
  (let [v (get-version)]
    (is (= (type (first (first v))) java.net.InetSocketAddress))
    (is (= (type (first (rest (first v)))) String))))

(deftest test-stats
  (let [s (get-stats)]
    (is (= (type (first (keys s))) java.net.InetSocketAddress))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
