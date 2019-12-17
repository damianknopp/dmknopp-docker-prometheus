# dmknopp-docker-prometheus
Sample prometheus and random scraper docker compose rig. This project follows the [Prometheus](https://prometheus.io/docs/prometheus/latest/getting_started/) getting started tutorial.

## Setup
Download

[Docker](https://docs.docker.com/v17.12/install/)

[Docker Compose](https://docs.docker.com/compose/install/)

## Run

```
docker-compose build
docker-compose up
```

Visit, `http://localhost:9090/targets`

You should see `UP` next to three targets on running on `8080, 8081, 8082`

## Notes

See Prometheus [getting started](https://prometheus.io/docs/prometheus/latest/getting_started/)

See example [go scraper](https://github.com/prometheus/client_golang)

