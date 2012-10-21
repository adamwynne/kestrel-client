# kestrel-client

Wrapper for spymemcached to access kestrel as a memcached client

## Usage

```Clojure

(:require [kestrel.client :as kestrel])

;;create client 
(kestrel/create-client)

;;get version of current Kestrel 
(kestrel/get-version)

;;add and get values from Kestrel
(kestrel/set-item "uris" "http://httpbin.org/ip")
(kestrel/set-item "uris" "http://httpbin.org/delay/10")

(kestrel/get-item "uris")
(nil? (kestrel/get-item "uris")) ;; should be false
(kestrel/get-item "uris") ;; should be nil 

;;flush queue 
(kestrel/flush-queue "uris")
(kestrel/flush-all) ;its nice to start working on clean sheet

;;get stat 

(kestrel/get-stats)

```

## License

Copyright (C) 2011 Adam Wynne (@AdamJWynne on twitter)

Distributed under the Eclipse Public License, the same as Clojure.
