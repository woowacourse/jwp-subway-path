# jwp-subway-path

### 1. 기능 목록

#### 1단계 기능 목록

- [x] 역 (Station)
  - [x] 역은 이름을 가지고 있다
  - [x] 이름은 한글과 숫자만 가능하고, 길이는 2글자 ~ 9글자이다
- [x] 거리 (Distance)
  - [x] 거리는 1 이상, 100 이하이어야 한다
- [x] 인접경로 (AdjustPath)
  - [x] 인접경로는 연결된 역과 거리를 가지고 있다
- [x] 노선 (Line)
  - [x] 노선 관리
    - [x] 노선을 등록할 수 있다
      - [x] 이름을 가지고 있다
        - [x] 이름은 한글과 숫자만 가능하고, 길이는 2글자 ~ 9글자이다
      - [x] 색깔을 가지고 있다
        - [x] 색깔은 `tailwindcss` 클래스 규칙을 따라야한다 (정규표현식: `^bg-[a-z]{3,7}-\d{2,3}$`)
      - [x] 노선은 역끼리의 정보를 가지고 있다
    - [x] 노선을 조회할 수 있다
    - [x] 노선을 수정할 수 있다
    - [x] 노선을 삭제할 수 있다
      - [x] 역이 있는 상태라도 노선을 삭제할 수 있다
  - [x] 역 관리
    - [x] 역을 등록할 수 있다
    - [x] 역을 조회할 수 있다
    - [x] 역을 수정할 수 있다
    - [x] 역을 삭제할 수 있다
  - [x] 노선과 역 관리
    - [x] 노선에 역을 등록할 수 있다
      - [x] 노선에 등록된 역이 없을 때, 두 개의 역을 동시에 등록해야 한다
      - [x] 노선을 갈래길을 가질 수 없다
    - [x] 노선에 역을 조회할 수 있다
    - [x] 노선에 역을 삭제할 수 있다
      - [x] 노선에 역이 2개일 경우, 하나의 역을 제거할 때, 모든 역을 삭제해야한다

#### 2단계 기능 목록

- [x] 데이터베이스 설정을 프로덕션과 테스트를 다르게 설정
- [x] 노선의 경로를 정렬하는 기능
- [x] 출발역과 도착역의 최단 거리를 구하는 기능
- [x] 경로를 조회하는 기능
- [x] 요금을 계산하는 기능

### 2. 테이블 구조

```sql
CREATE TABLE IF NOT EXISTS line
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    color VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS station
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS line_station
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    line_id BIGINT NOT NULL,
    station_id BIGINT NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS section
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    line_id BIGINT NOT NULL,
    up_station_id BIGINT NOT NULL,
    down_station_id BIGINT NOT NULL,
    PRIMARY KEY(id)
);
```

### 3. API 문서

[Swagger 문서](http://localhost:8080/swagger-ui/index.html)

- 역 (`/stations`)
  - 역 등록 `POST / 201 Location/stations/{id}`
  - 역 단일 조회 `GET /{id} 200`
  - 역 삭제 `DELETE / 204`
- 노선 (`/lines`)
  - 노선 등록 `POST / 201 Location/stations/{id}`
  - 노선 단일 조회 `GET /{id} 200`
  - 노선 전체 조회 `GET / 200`
  - 노선 수정 `PATCH /{id} 200`
  - 노선 삭제 `DELETE / 204`
- 노선 - 역 등록 (`/lines/{lineId}/stations`)
  - 처음 역 등록 `POST /init 201 /`
  - 역 상행/하행 등록 `POST /end 201 /`
  - 중간 역 등록 `POST /middle 201 /`
  - 역 전체 삭제 `DELETE /all 204`
  - 역 종점 삭제 `DELETE /end 204`
  - 중간 역 삭제 `DELETE /middle 204`
- 경로 (`/path`)
  - 경로와 요금 조회 `GET / 200`
