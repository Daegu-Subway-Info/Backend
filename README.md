# Daegu Metro Route Navigation & Timetable API

대구 도시철도 경로 탐색 및 열차 시간표 안내 시스템의 Backend 서버입니다.

React 기반 Front-End와 REST API로 통신하며, PostgreSQL에 저장된 노선 정보를 이용하여 최적 경로를 계산하고 대구교통공사 Open API를 연동하여 열차 시간표를 제공합니다.

---

# Tech Stack

| Category | Technology |
|----------|------------|
| Language | Java 21 |
| Framework | Spring Boot 3 |
| Build Tool | Gradle |
| Database | PostgreSQL |
| ORM | Spring Data JPA |
| Query | QueryDSL |
| Validation | Spring Validation |
| Utility | Lombok |

---

# Project Structure

```text
src/main/java/com/daegumetro

├── common
│
├── line
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   └── dto
│
├── station
│
├── route
│
├── timetable
│
├── search
│
└── external
    └── dtro
```

---

# Architecture

```text
React

↓

REST API (Axios)

↓

Spring Boot

├── Line Service
├── Station Service
├── Route Service
├── Timetable Service
└── Search Service

↓

PostgreSQL

+

DTRO Open API
```

---

# Features

## Line

- 전체 노선 조회
- 노선별 역 조회

## Station

- 전체 역 조회
- 역 상세 조회
- 역 검색
- 자동완성

## Route

- 최적 경로 탐색
- 환승 횟수 계산
- 예상 소요시간 계산
- 예상 요금 계산

## Timetable

- 시간표 조회
- 첫차 조회
- 막차 조회
- 다음 열차 조회

---

# API

## Line

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/lines` | 전체 노선 조회 |
| GET | `/api/lines/{lineId}` | 노선별 역 조회 |

---

## Station

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/stations` | 전체 역 조회 |
| GET | `/api/stations/{stationId}` | 역 상세 조회 |
| GET | `/api/stations/search` | 역 검색 |

---

## Route

| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/routes` | 최적 경로 탐색 |

---

## Timetable

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/timetables` | 시간표 조회 |
| GET | `/api/timetables/first-last` | 첫차 / 막차 조회 |
| GET | `/api/timetables/next` | 다음 열차 조회 |

---

# External API

대구교통공사 Open API를 이용하여 시간표 정보를 제공합니다.

```http
GET
https://www.dtro.or.kr/open_content_new/ko/OpenApi/stationTime.php
```

### Request Parameters

| Parameter | Description |
|-----------|-------------|
| STT_NM | 역명 |
| LINE_NO | 호선 |
| SCHEDULE_METH | 상행/하행 |
| SCHEDULE_TYPE | 평일/토요일/공휴일 |

---

# Route Algorithm

경로 탐색은 그래프(Graph) 기반의 Dijkstra 알고리즘을 사용합니다.

### 입력

- 출발역
- 도착역

### 출력

- 이동 경로
- 환승 횟수
- 예상 소요시간
- 예상 요금

---

# Getting Started

## Clone

```bash
git clone https://github.com/your-repository/backend.git
```

## Build

```bash
./gradlew build
```

## Run

```bash
./gradlew bootRun
```

---

# Database

PostgreSQL을 사용합니다.

`application.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/daegumetro
    username: postgres
    password: password

  jpa:
    hibernate:
      ddl-auto: update
```

---

# Progress

- [x] 프로젝트 초기 설정
- [ ] 노선 API 구현
- [ ] 역 조회 API 구현
- [ ] 길찾기 API 구현
- [ ] 시간표 API 연동
- [ ] 첫차/막차 계산
- [ ] 다음 열차 계산

---

# Developers

Capstone Design Project

- Backend : Spring Boot
- Frontend : React
- Database : PostgreSQL