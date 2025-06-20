# Kubernetes

First ensure Minikube and helm is installed.

```shell
minikube start --driver=docker
```

deploy the secrets:

```shell
kubectl apply -f secrets.yaml
```


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

get your cluster ip:

```shell
minikube ip
```

Now you can access the sites on

- **Backend**: <minikube-ip>:30081/swagger-ui/index.html
- **Frontend**: <minikube-ip>:30080/login


2. Minikube & Helm Installation



Install Minikube:

curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
sudo install minikube-linux-amd64 /usr/local/bin/minikube
rm minikube-linux-amd64

Start Minikube cluster:

minikube start --driver=docker

Install Helm:

curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3
chmod 700 get_helm.sh
./get_helm.sh
helm version # Verify installation



4. Deploy Monitoring Stack (Prometheus & Grafana)
   Use the kube-prometheus-stack Helm chart for a complete monitoring setup.

Add Helm repo & update:

helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update

Create monitoring namespace:

kubectl create namespace monitoring

Install kube-prometheus-stack:

helm install prometheus-stack prometheus-community/kube-prometheus-stack --namespace monitoring

Monitor deployment:

kubectl get pods -n monitoring --watch # Ctrl+C to exit

5. Access Grafana
   Get Grafana admin password:

kubectl get secret --namespace monitoring prometheus-stack-grafana -o jsonpath="{.data.admin-password}" | base64 --decode ; echo
# Copy this password!

Port-forward Grafana UI:

kubectl port-forward service/prometheus-stack-grafana 3000:80 -n monitoring
# Keep this terminal open

Open in browser: http://localhost:3000

Log in: Username admin, Password (from step 1).

6. Verification & Usage
   Once in Grafana:

Verify Prometheus Data Source: Check Configuration (gear) -> Data Sources. Prometheus should be working.

Explore Dashboards: Navigate to Dashboards (square icon) -> Manage. View pre-built Kubernetes dashboards.

Create/Import Backend Dashboard:

Create (plus) -> Dashboard -> Add new panel.

Select Prometheus data source.

Query Spring Boot metrics, e.g., process_cpu_usage{app="backend"} * 100.

Import JVM/Spring Boot dashboards from Grafana Labs (e.g., ID 4701).

7. Cleanup (Optional)
   Stop Minikube (retains data):

minikube stop

Uninstall monitoring stack:

helm uninstall prometheus-stack --namespace monitoring
kubectl delete namespace monitoring

Delete Minikube cluster (all data lost):

minikube delete
