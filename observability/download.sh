cat > namespace.yaml <<EOF
---
apiVersion: v1
kind: Namespace
metadata:
  name: observability
EOF

wget https://raw.githubusercontent.com/jaegertracing/jaeger-operator/v1.17.1/deploy/service_account.yaml
wget https://raw.githubusercontent.com/jaegertracing/jaeger-operator/v1.17.1/deploy/cluster_role.yaml
wget https://raw.githubusercontent.com/jaegertracing/jaeger-operator/v1.17.1/deploy/cluster_role_binding.yaml
wget https://raw.githubusercontent.com/jaegertracing/jaeger-operator/v1.17.1/deploy/crds/jaegertracing.io_jaegers_crd.yaml
wget https://raw.githubusercontent.com/jaegertracing/jaeger-operator/v1.17.1/deploy/operator.yaml
