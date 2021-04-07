(defproject record-sort-gr "0.1.0-SNAPSHOT"
  :description "A system to parse and sort a set of records."
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler record-sort-gr.handler/app}
  :source-paths ["src"]
  :test-paths ["test"]
  :target-path "target/%s"
  :main ^:skip-aot record-sort-gr.core
  
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
             :dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.3.2"]]}}
  :test-selectors {:default (complement :disabled)})
