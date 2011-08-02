(defproject kestrel-tester "1.0.0-SNAPSHOT"
  :description "tester for kestrel"
  :repositories {"spy" "http://files.couchbase.com/maven2/"}
  :dev-dependencies [[swank-clojure "1.3.2"]]
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [spy/spymemcached "2.7"]])