(defproject record-sort-gr "0.1.0-SNAPSHOT"
  :description "A system to parse and sort a set of records."
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[compojure "1.6.2"]
                 [org.clojure/clojure "1.10.1"]
                 [ring "1.8.2"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.5.1"]
                 ]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler record-sort-gr/app}
  :source-paths ["src"]
  :test-paths ["test"]
  :target-path "target/%s"
  :uberjar-name "record-sort-gr.jar"
  :main ^:skip-aot record-sort-gr.core
  :repl-options {:init-ns record-sort-gr.core}
  
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
             :dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.3.2"]]}}
  :test-selectors {:default (complement :disabled)})
