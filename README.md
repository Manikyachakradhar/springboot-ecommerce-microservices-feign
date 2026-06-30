# Spring Boot E-Commerce Microservices

A production-style e-commerce backend built using Spring Boot Microservices architecture. The project demonstrates service-to-service communication, service discovery, API gateway routing, fault tolerance, and distributed application design.

## Tech Stack

* Java 21
* Spring Boot 3.5
* Spring Data JPA (Hibernate)
* PostgreSQL
* Spring Cloud OpenFeign
* Spring Cloud Gateway
* Netflix Eureka Service Discovery
* Resilience4j Circuit Breaker
* Spring Boot Actuator
* Maven

## Microservices

* Product Service
* Cart Service
* Order Service
* API Gateway
* Eureka Server

## Features

### Product Service

* Product Management (CRUD)
* Stock Management
* Stock Validation

### Cart Service

* Add Product to Cart
* Update Cart
* Remove Product from Cart
* Clear Cart
* Cart Total Calculation

### Order Service

* Create Order
* Checkout Flow
* Stock Deduction
* Order Total Calculation

## Microservices Features

* Service Discovery using Netflix Eureka
* API Gateway for centralized routing
* Service-to-Service Communication using OpenFeign
* Fault Tolerance using Resilience4j Circuit Breaker
* Fallback Handling for downstream service failures
* Global Exception Handling
* Health Monitoring using Spring Boot Actuator

## Architecture

Client
↓
API Gateway
↓
Product Service
Cart Service
Order Service
↓
PostgreSQL

## Upcoming Enhancements

* JWT Authentication & Authorization
* Docker Containerization
* AWS Deployment
* CI/CD using GitHub Actions
