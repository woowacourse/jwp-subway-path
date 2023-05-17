# jwp-subway-path

## 1단계

## 🎯 기능 목록

- [x]  노선에 역 추가 API 구현
    - [x]  노선에 첫 역 추가 API 및 기능 구현
    - [x]  역이 있는 노선에 역 추가 API 및 기능 구현
- [ ]  노선에 역 제거 API 구현
    - [ ]  마지막 두 역이 아닌 역 삭제 기능 구현
    - [ ]  마지막 두 역 중 하나를 삭제할 시 모든 역 삭제 기능 구현
- [x]  노선별 조회 기능에 역 조회 기능 추가
- [ ]  모든 노선 조회 기능에 각 노선별 역 조회 기능 추가

---

## 🛠 설계

### DB

- line

| column      | type         |                    |
|-------------|--------------|--------------------|
| id          | BIGINT       | PK, AUTO_INCREMENT |
| name        | VARCHAR(255) | NOT NULL, UNIQUE   |
| color       | VARCHAR(20)  | NOT NULL           |
| endpoint_id | BIGINT       |                    |

- station

| column | type         |                    |
|--------|--------------|--------------------|
| id     | BIGINT       | PK, AUTO_INCREMENT |
| name   | VARCHAR(255) | NOT NULL, UNIQUE   |

- section

| column          | type   |                    |
|-----------------|--------|--------------------|
| id              | BIGINT | PK, AUTO_INCREMENT |
| line_id         | BIGINT | NOT NULL           |
| up_station_id   | BIGINT | NOT NULL           |
| down_station_id | BIGINT | NOT NULL           |
| distance        | INT    | NOT NULL           |

### API

- POST /lines/{id}/station/init
- POST /lines/{id}/station/{stationId}
- DELETE /lines/{id}/station/{stationId}
- GET /lines
- GET /lines/{id}
  ️
