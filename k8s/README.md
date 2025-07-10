## Kubernetes Deployment with Helm

This project uses Helm charts for deploying MongoDB, Grafana, Loki, and Prometheus.

### Prerequisites

*   **Helm:** Ensure Helm is installed on your system. Refer to the official Helm documentation for installation instructions: [https://helm.sh/docs/intro/install/](https://helm.sh/docs/intro/install/)
*   **Minikube:** Ensure Minikube is installed and configured.

### Setting up the entire application stack on Minikube

To deploy the entire application stack (MongoDB, Grafana, Loki, Prometheus, Backend, Frontend, and Background Service) on Minikube, follow these steps:

1.  **Start Minikube:**
    ```bash
    minikube start
    ```

2.  **Add Helm Repositories:**
    Before installing the charts, add the necessary Helm repositories:
    ```bash
    helm repo add bitnami https://charts.bitnami.com/bitnami
    helm repo add grafana https://grafana.github.io/helm-charts
    helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
    helm repo update
    ```

3.  **Apply Application Secrets:**
    Apply the secrets required for the application components.
    ```bash
    kubectl apply -f k8s/app-secrets.yaml
    ```

4.  **Install Helm Charts:**
    Each service has its own `values.yaml` file in its respective directory under `k8s/`. These files contain common configurations like storage and default passwords. **Remember to review and update sensitive values like passwords before deployment.**

    *   **MongoDB:**
        ```bash
        helm install mongodb bitnami/mongodb -f k8s/mongodb/values.yaml --namespace default
        ```

    *   **Grafana:**
        ```bash
        helm install grafana grafana/grafana -f k8s/grafana/values.yaml --namespace default
        ```

    *   **Loki:**
        ```bash
        helm install loki grafana/loki -f k8s/loki/values.yaml --namespace default
        ```

    *   **Prometheus:**
        ```bash
        helm install prometheus prometheus-community/prometheus -f k8s/prometheus/values.yaml --namespace default
        ```

5.  **Deploy Application Services:**
    Deploy the backend, frontend, and background services using their respective Kubernetes manifests.

    *   **Backend Service:**
        ```bash
        kubectl apply -f k8s/backend/backend-deployment.yaml
        kubectl apply -f k8s/backend/backend-service.yaml
        ```

    *   **Frontend Service:**
        ```bash
        kubectl apply -f k8s/frontend/frontend-deployment.yaml
        kubectl apply -f k8s/frontend/frontend-service.yaml
        ```

    *   **Background Service:**
        ```bash
        kubectl apply -f k8s/background-service/background-service-deployment.yaml
        kubectl apply -f k8s/background-service/background-service-service.yaml
        ```

### Accessing the Services

Once all services are deployed, you can access them using `minikube service` commands:

*   **Frontend:**
    ```bash
    minikube service frontend
    ```
    This will open the frontend in your browser.

*   **Backend API Docs (Swagger UI):**
    ```bash
    minikube service backend --url
    ```
    Append `/swagger.html` to the URL to access the API documentation (e.g., `http://<backend-ip>:<port>/swagger.html`).

*   **Grafana:**
    ```bash
    minikube service grafana --url
    ```
    Use the `admin` username and the password configured in `k8s/grafana/values.yaml` (default is `your-secure-grafana-password`).

### Custom Metrics (Backend)

The backend service now exposes custom metrics via Micrometer, specifically tracking task-related operations:

*   `tasks_total`: A gauge representing the total number of tasks.
*   `tasks_created_total`: A counter for the total number of tasks created.
*   `tasks_deleted_total`: A counter for the total number of tasks deleted.

These metrics are exposed through the `/actuator/prometheus` endpoint and are scraped by Prometheus.

### Grafana Dashboard Provisioning

When using the official Grafana Helm chart, dashboards can be automatically provisioned by creating Kubernetes ConfigMaps with the `grafana_dashboard` label. The Grafana chart is configured to discover and load dashboards from these ConfigMaps. 

This project includes a comprehensive pre-configured dashboard for the backend service. It is defined in `k8s/grafana/backend-dashboard-configmap.yaml` and will be automatically provisioned by Grafana. This dashboard displays:

*   **Backend Logs:** Logs from the backend service (assuming JSON format).
*   **Custom Task Metrics:** Visualizations for `tasks_total`, `tasks_created_total`, and `tasks_deleted_total`.
*   **JVM Metrics:** Detailed metrics on CPU usage, various memory pools (heap, non-heap, committed, max), garbage collection (pause count, live data size, max data size), and thread counts (live, daemon, peak).
*   **System Metrics:** System CPU usage and CPU core count.
*   **Process Metrics:** Process uptime and open file descriptors.
*   **HTTP Request Metrics:** Insights into HTTP request counts and durations.

### Backend Labels for Monitoring

The `backend-deployment.yaml` has been updated to include labels for Prometheus and Loki to scrape metrics and logs. This allows Prometheus to discover and scrape metrics from the backend service, and Loki to collect logs.