(ns kestrel.test.client
  (:use
   [clojure.test])
  (:import
   (net.spy.memcached MemcachedClient)
   (java.net InetSocketAddress)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest test-create-client
  (is (= (type (MemcachedClient. (list (InetSocketAddress. "localhost" 22133))))
         net.spy.memcached.MemcachedClient)))

(deftest test-set-get
  (let [mcd (MemcachedClient. (list (InetSocketAddress. "localhost" 22133)))
        q-name "testq"]
    (.set mcd q-name 0 "testing")
    (is (= (.get mcd q-name) "testing"))))

(deftest test-range
  (let [mcd (MemcachedClient. (list (InetSocketAddress. "localhost" 22133)))
        q-name "testq"]
    (doseq [i (range 10)] (.set mcd q-name 0 (str i)))
    ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
