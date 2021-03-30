(defproject record-sort-gr "0.1.0-SNAPSHOT"
  :description "A system to parse and sort a set of records."
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :source-paths ["src/clj" "src/cljc" "src/cljs"]
  :test-paths ["test/clj"]
  :target-path "target/%s"
  :main ^:skip-aot record-sort-gr.core
  
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
  :test-selectors {:default (complement :disabled)})
