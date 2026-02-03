```mermaid
graph TD
    User(("User/Browser")) -->|"HTTPS Requests"| API["Spring Boot App <br> Port: 8085"]

    subgraph "Observability Stack"
        API -->|"Metrics (AOP)"| Prom["Prometheus <br> (Scrapes every 15s)"]
        API -->|"Logs (Logback)"| Loki["Loki <br> (Log Aggregation)"]

        Prom -->|"Data Source"| Grafana["Grafana Dashboard"]
        Loki -->|"Data Source"| Grafana
    end

    Grafana -->|Visualizes| Dev["Developer (You)"]

    style API fill:#6db33f,stroke:#333,stroke-width:2px,color:white
    style Grafana fill:#f47a20,stroke:#333,stroke-width:2px,color:white
    style Prom fill:#e6522c,stroke:#333,stroke-width:2px,color:white
    style Loki fill:#f5cd2d,stroke:#333,stroke-width:2px,color:black