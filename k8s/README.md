

add the helm chart and install it:

```shell
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
helm install mongodb bitnami/mongodb -f db/values.yaml
```

deploy the other stuff

```shell
kubectl apply -f backend/
kubectl apply -f frontend/
kubectl apply -f svc/
```