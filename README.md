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

--------- 0.2 ---------------

Objectives
- adding another 'actuator' service
- making the new service seding spans to Jaeger
- routing requests from 'gateway' to 'actuator'

# Adding another service (version 0.2 mirrors the current state of things)

## 4./ apps
1. create a regular deployment yaml file
2. add an annotation to the deployment with value: `sidecar-jaegertracing.io/inject: <jaeger-name>` // it tells the operator to inject a sidecar container for the jaeger agent
3. add `opentracing-spring-jaeger-web-starter` dependency
4. publish new tag image (`docker build . --tag michalwit/spring-only-actuator:1.0` and `docker image push michalwit/spring-only-actuator:1.0`)
5. update deployment file with new image tag and redeploy in kubernetes
6. port-forward to the gateway pod, and make a request(`kubectl port-forward -n apps gateway-788547d7d8-845lj 9999 9999` and curl localhost:9999/get`)

## 5. /apps (expose a service, so that DNS can be levereged)
1. use kubectl to create a service using spring-only-actuator-service.yaml file
2. use `kubectl run` to create a busybox pod in the same namespace (apps) and use telent to check connection

## 6. /apps (use spring-cloud-gateway:1.2 with routing to 'actuator' service)
1. `kubectl delete -f'` , apply change to the version and then `kubectl create -f
2. use port forwarding `kubectl port-forward -n apps <gateway-pod> 9999 9999` and `curl localhost:9999/routed` to see that the message commes from the 'actuator' service




--------------------- 0.3 ------------

Objectives:
- combining the two separate into one trace (the actual fix)
- adding another service behind 'gateway' calling the 'actuator' service : G -> intermediary -> A

# Send trace ID from gateway and adding another 'intermediary' service

## 7. Send trace ID from gateway
1. Switch `opentracing-spring-jaeger-web-starter` with `opentracing-spring-jaeger-cloud-starter` dependency in a `gateway` (it maked the Jaeger to be aware of the actual trace) (gateway v 0.3)
2. Add `management.endpoints.web.exposure.include: "httptrace"` to `actuator` service to be able to see rereceived requests and implement a `HttpTraceRepository` (actuator v 0.3)
