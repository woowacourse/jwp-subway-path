# jwp-subway-path

### 1. 기능 목록

- [ ] 역 (Station)
  - [ ] 역은 이름을 가지고 있다
  - [ ] 이름은 한글과 숫자만 가능하고, 길이는 2글자 ~ 9글자이다
- [ ] 거리 (Distance)
  - [ ] 거리는 1 이상이어야 한다
- [ ] 인접경로 (AdjustPath)
  - [ ] 인접경로는 연결된 역과 거리를 가지고 있다
- [ ] 노선 (Line)
  - [ ] 노선 관리
    - [ ] 노선을 등록할 수 있다
      - [ ] 이름을 가지고 있다
        - [ ] 이름은 한글과 숫자만 가능하고, 길이는 2글자 ~ 9글자이다
      - [ ] 색깔을 가지고 있다
        - [ ] 색깔은 `tailwindcss` 클래스 규칙을 따라야한다 (정규표현식: `^bg-[a-z]{3,7}-\d{2,3}$`)
      - [ ] 노선은 역끼리의 정보를 가지고 있다
    - [ ] 노선을 조회할 수 있다
    - [ ] 노선을 수정할 수 있다
    - [ ] 노선을 삭제할 수 있다
      - [ ] 역이 있는 상태라도 노선을 삭제할 수 있다
  - [ ] 역 관리
    - [ ] 역을 등록할 수 있다
    - [ ] 역을 조회할 수 있다
    - [ ] 역을 수정할 수 있다
    - [ ] 역을 삭제할 수 있다
  - [ ] 노선과 역 관리
    - [ ] 노선에 역을 등록할 수 있다
      - [ ] 노선에 등록된 역이 없을 때, 두 개의 역을 동시에 등록해야 한다
      - [ ] 노선을 갈래길을 가질 수 없다
    - [ ] 노선에 역을 조회할 수 있다
    - [ ] 노선에 역을 삭제할 수 있다
      - [ ] 노선에 역이 2개일 경우, 하나의 역을 제거할 때, 모든 역을 삭제해야한다 
- [ ] 노선도 (Route Map)
  - [ ] 노선도는 노선들을 가지고 있다
    - [ ] 노선의 이름과 색깔은 각각 고유한 값이어야 한다

2. 테이블 구조

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

3. API 문서
- 역 (`/stations`)
  - 역 등록 `POST / 201 Location/stations/{id}`
  - 역 단일 조회 `GET /{id} 200`
  - 역 전체 조회 `GET / 200`
  - 역 수정 `PATCH /{id} 200`
  - 역 삭제 `DELETE / 204`
- 노선 (`/lines`)
  - 노선 등록 `POST / 201 Location/stations/{id}`
  - 노선 단일 조회 `GET /{id} 200`
  - 노선 전체 조회 `GET / 200`
  - 노선 수정 `PATCH /{id} 200`
  - 노선 삭제 `DELETE / 204`
- 노선 - 역 등록 (`/lines/{lineId}/stations`)
  - 처음 역 등록 `POST /regist/new/{upStationId}/{downStationId} 201 /`
  - 역 상행/하행 등록 `POST /regist/end/{originStationId}/{newStationId} 201 /`
  - 역 중간 등록 `POST /regist/middle/{upStationId}/{downStationId}/{newStationId} 201 /`
  - 역 삭제 `DELETE /{stationId} 204`
