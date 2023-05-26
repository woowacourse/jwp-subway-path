# 요구사항

## 테이블 설계

### ✔ 역 (STATIONS)

| 컬럼명  | 데이터 타입      | 속성     |
|------|-------------|--------|
| ID   | BIGINT      | PK, AI |
| NAME | VARCHAR(16) | NN     |

### ✔ 구간(SECTIONS)

| 컬럼명             | 데이터 타입 | 속성    |
|-----------------|--------|-------|
| ID              | BIGINT | PK,AI |
| LINE_ID         | BIGINT | NN,FK |
| UP_STATION_ID   | BIGINT | NN,FK |
| UP_STATION_ID   | BIGINT | NN,FK |
| DOWN_STATION_ID | BIGINT | NN,FK |
| DISTANCE        | INT    | NN    |

### ✔ 노선(LINES)

| 컬럼명   | 데이터 타입      | 속성     |
|-------|-------------|--------|
| ID    | BIGINT      | PK, AI |
| NAME  | VARCHAR(16) | NN     |
| COLOR | VARCHAR(32) | NN     |

## 🗣 API

### ✔ **역 API**

| 기능      | Method | URL            | Body |
|---------|--------|----------------|------|
| 생성      | POST   | /stations      | name |
| 단일 - 조회 | GET    | /stations/{id} |      |
| 전체 - 조회 | GET    | /stations      |      |
| 수정      | PATCH  | /stations/{id} | name |
| 삭제      | DELETE | /stations/{id} |      |

### ✔ 노선 API

| 기능      | Method | URL         | Body        |
|---------|--------|-------------|-------------|
| 생성      | POST   | /lines      | name, color |
| 단일 - 조회 | GET    | /lines/{id} |             |
| 전체 - 조회 | GET    | /lines      |             |
| 수정      | PATCH  | /lines/{id} | name, color |
| 삭제      | DELETE | /lines/{id} |             |

### ✔ 노선 - 구간 API

| 기능 | Method | URL                  | Body                                 |
|----|--------|----------------------|--------------------------------------|
| 생성 | POST   | /lines/{id}/sections | upStationId, downStationId, distance |
| 삭제 | DELETE | /lines/{id}/sections | stationId                            |

### ✔ 경로-요금 API

| 기능 | Method | URL   | Body                                |
|----|--------|-------|-------------------------------------|
| 조회 | POST   | /path | departureStationId, arriveStationId |

## 🚇 도메인

### 역

- 역은 ID와 이름과 여러 노선 ID, 최대 2개의 구간 ID를 가진다.
- 새로운 역을 추가할 수 있다.
    - 노선 정보도 같이 받는다.
    - 역을 노선에 추가할 때는 기준이 있어야 한다.
        - 예시) B-C 사이, B 뒤, C 앞
    - 역과 역 사이의 거리는 양의 정수
- 기존 역을 삭제할 수 있다.
    - 이때, 앞 뒤 역을 연결해야한다.
- 역을 최초로 노선에 등록할 때는 2개를 입력받아야한다.
    - 거리 정보도 같이 받는다.
- 역을 제거할 때 만약 남은 역이 2개이면 제거할 수 없다.
    - 만약에 제거해야한다면 다 제거해야한다.

### 구간

- ID와 거리를 가진다.

### 노선

- ID와 이름과 색상을 가진다.

### 요금

- [기본운임] 10km 이내 : 1,250원
- [추가운임]
    - 10~50km 이내 : 5km 까지 마다 100원 추가
    - 50km 초과 : 8km 까지 마다 100원 추가

### 경로 조회

- 출발, 도착역 ID를 받아 최소 경로를 계산한다
    - 환승도 고려한다.
