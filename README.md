# Real-time Chat System with Kafka

A distributed chat application built with Spring Boot, WebSocket, and Apache Kafka for message processing and email notifications.

## Project Structure

```
├── kafka_services_api/          # Message Producer Service
│   ├── src/
│   │   ├── main/java
│   │   │   ├── config/         # Kafka and WebSocket configurations
│   │   │   ├── controller/     # REST and WebSocket endpoints
│   │   │   └── dto/           # Data transfer objects
│   │   └── resources/         # Application properties
│   └── pom.xml
│
├── core/                      # Message Consumer Service
│   ├── src/
│   │   ├── main/java
│   │   │   ├── config/       # Kafka consumer configurations
│   │   │   ├── service/      # Message processing services
│   │   │   └── dto/         # Data transfer objects
│   │   └── resources/       # Application properties
│   └── pom.xml
│
└── frontend/                 # Web Interface
    ├── css/
    │   └── styles.css
    ├── js/
    │   └── script.js
    └── index.html
```

## Features

- Real-time messaging using WebSocket
- Message persistence with Apache Kafka
- Email notifications for messages
- Message consumer service for processing
- Simple and intuitive chat interface
- Cross-origin support for frontend integration

## Tech Stack

### Backend Services
- Spring Boot
- WebSocket (STOMP)
- Apache Kafka
- Java Mail Sender
- Spring Kafka

### Frontend
- HTML/CSS
- JavaScript
- SockJS
- STOMP.js

## Service Components

### Kafka Services API (Producer)
- Handles incoming messages
- Manages WebSocket connections
- Produces messages to Kafka topics
- Real-time message broadcasting

### Core Service (Consumer)
- Consumes messages from Kafka
- Processes message content
- Sends email notifications
- Handles message persistence

## Getting Started

1. Start Kafka and Zookeeper servers
2. Configure application properties in both services
3. Start the Core service (consumer)
4. Start the Kafka Services API (producer)
5. Open frontend/index.html in a browser

## Configuration

### Kafka Services API
```properties
server.port=9090
spring.kafka.bootstrap-servers=localhost:9092
request.message.topic=message-topic
```

### Core Service
```properties
server.port=8080
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=email-group
```

## API Endpoints

- `POST /message` - Send a new message
- `WS /ws` - WebSocket connection endpoint

## WebSocket Topics

- `/topic/messages` - Public message channel
- `/user/queue/private` - Private messages

## Development

1. Clone the repository
2. Configure Kafka connection in both services
3. Set up email configuration in core service
4. Run both Spring Boot applications
5. Open the frontend in a web browser

## Requirements

- Java 11+
- Apache Kafka
- Modern web browser
- Maven
