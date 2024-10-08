# Social Media Microservices Application

## Overview
This project is a **social media platform** built using a microservice architecture with **Spring Boot** and an **event-driven architecture** using **Kafka**. It allows users to create posts, upload media, interact with posts, and see a feed of interactions.

## Key Features
- **Post Service**: Users can create, edit, delete, and retrieve posts.
- **Media Service**: Allows uploading and managing media files.
- **Interaction Service**: Handles user interactions such as likes, comments, and shares on posts.
- **Feed Service**: Displays a real-time feed of posts and interactions.
- **Event-Driven Architecture**: Communication between microservices is handled via Kafka to ensure scalability and decoupled services.
- **Microservice Architecture**: Each service is developed and deployed independently.

## Tech Stack
- **Backend**: Java, Spring Boot, Spring Data JPA
- **Message Broker**: Apache Kafka
- **Database**: PostgreSQL
- **Containerization**: Docker
- **Deployment**: Kubernetes
- **CI/CD**: Jenkins
- **Cloud**: AWS

## Project Structure
This application is structured in multiple microservices, each handling specific features:

- `post-service`: Manages user posts.
- `media-service`: Handles media uploads and processing.
- `interaction-service`: Tracks user interactions like comments and likes.
- `feed-service`: Aggregates data and creates user feeds.

### Event-Driven Communication
All services communicate through Kafka topics:
- `post_topic`: Used by the Post service to broadcast post events.
- `media_topic`: Used by the Media service for media upload events.
- `interaction_topic`: Used by the Interaction service for like/comment/share events.

### Dependencies
- **Java 17**
- **Spring Boot 3**
- **Kafka**
- **Docker**
- **Kubernetes**
- **AWS SDK**

## Getting Started

### Prerequisites
- Docker and Docker Compose installed.
- Kubernetes setup.
- Kafka broker and Zookeeper.
- PostgreSQL database.
- AWS S3 for media storage.
