# Run EcommerceBackendApplication

Run the `EcommerceBackendApplication` class in your IDE.

# Swagger UI
Access http://localhost:8081/swagger-ui/index.html

# Metrics (Prometheus)
In command line, run the following commands:

docker pull prom/prometheus

docker run -p 9090:9090 -v 'YOUR ABSOLUTE PATH TO :src\main\resources\prometheus.yml' prom/prometheus

Access localhost:9090

List of Prometheus metric queries:

    createCart_calls_total
    getCart_calls_total
    getCart_calls_success
    getCart_calls_failure
    addProductToCart_calls_total
    addProductToCart_calls_success
    addProductToCart_calls_failure
    deleteCart_calls_total
    deleteCart_calls_success
    deleteCart_calls_failure
    carts_deleted
    carts_expired



