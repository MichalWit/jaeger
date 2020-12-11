## 1. /observability
1. Use 'download.sh'
2. Use 'kubectl' to create resources - all instead of 00nemspace.yaml with '-n observability' arg
3. Replace value for 'WATCH_NAMESPACE' in observer.yaml to empty string "" - to watch for all namespaces

## 2. /tools
1. Add ingress:false to the Jaeger
2. add namespace:tools
3. Run 'jaeger.yaml' to create the actual jaeger (query, collector, collector-headless, agent)
4. Use 'kubectl port-forward' to access the 'query' component, e.g. `k port-forward -n tools pod/jaeger-7bc8bb4cd8-9nnb4 16686 16686` - use proper pod name - now the UI should be accessble on localhost:16686

## 3. /apps
1. create a regular deployment yaml file
2. add an annotation to the deployment with value: `sidecar-jaegertracing.io/inject: <jaeger-name>` // it tells the operator to inject a sidecar container for the jaeger agent
3. use kubect to create the deployment`
4. add `opentracing-spring-jaeger-web-starter` dependency (no changes in application.yml required! the defaults work correctly!)
5. publish new tag image (`docker build . --tag michalwit/spring-cloud-gateway:1.1` and `docker image push michalwit/spring-cloud-gateway:1.1`)
6. update deployment file with new image tag and redeploy in kubernetes
7. port-forward to the gateway pod, and make a request(`kubectl port-forward -n apps gateway-788547d7d8-845lj 9999 9999` and curl localhost:9999/get`)

------------------------

# Adding another service

1. create a regular deployment yaml file
2. add an annotation to the deployment with value: `sidecar-jaegertracing.io/inject: <jaeger-name>` // it tells the operator to inject a sidecar container for the jaeger agent
3. add `opentracing-spring-jaeger-web-starter` dependency 
4. publish new tag image (`docker build . --tag michalwit/spring-only-actuator:1.0` and `docker image push michalwit/spring-only-actuator:1.0`)
5. update deployment file with new image tag and redeploy in kubernetes
6. port-forward to the gateway pod, and make a request(`kubectl port-forward -n apps gateway-788547d7d8-845lj 9999 9999` and curl localhost:9999/get`)

