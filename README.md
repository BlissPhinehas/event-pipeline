Then paste this in:
markdown# Event Pipeline

A distributed backend system that ingests a high-volume stream of events, processes them in real time, and exposes a REST API showing live aggregated stats.

## What It Does

A Kafka producer continuously generates events (clicks, purchases, searches, views) and publishes them to a Kafka topic. A Spring Boot consumer reads from that topic, aggregates stats like counts and averages in 5-second windows, and stores the results in PostgreSQL. A REST API exposes the live stats.

## Tech Stack

- Java 21
- Spring Boot 3.5
- Apache Kafka
- PostgreSQL
- Docker and Docker Compose
- AWS EC2
- GitHub Actions CI/CD

## Architecture
Kafka Producer → Kafka Topic → Spring Boot Consumer → PostgreSQL → REST API

## Running Locally

Make sure you have Docker and Docker Compose installed, then run:

```bash
docker compose up --build
```

That single command starts Zookeeper, Kafka, PostgreSQL, and the Spring Boot app together.

## API Endpoints

Get all aggregated stats:
GET http://localhost:8080/api/stats

Get stats for a specific event type:
GET http://localhost:8080/api/stats/{eventType}

Event types are: click, purchase, search, view

Get a 60-second summary:
GET http://localhost:8080/api/stats/summary

Example response from /api/stats/summary:
```json
{
  "totalEvents": 130,
  "periodSeconds": 60,
  "countsByType": {
    "click": 34,
    "purchase": 28,
    "search": 31,
    "view": 37
  },
  "windows": []
}
```

## Health Check
GET http://localhost:8080/actuator/health

## CI/CD

Every push to main triggers a GitHub Actions workflow that builds the project with Maven and pushes a Docker image to Docker Hub at blissphinehas/event-pipeline.

## AWS Deployment

The app is deployable to an AWS EC2 instance. Clone the repo on the server, then run:

```bash
docker compose up -d
```

Note: Running all services together requires at least a t3.small instance due to Kafka memory requirements.

## Project Structure
src/
main/
java/com/pipeline/event_pipeline/
producer/       Kafka event producer
consumer/       Kafka event consumer
model/          JPA entity
repository/     Spring Data repository
service/        Stats aggregation logic
controller/     REST API endpoints

