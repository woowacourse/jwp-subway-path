# jwp-subway-path

### 1. 기능 목록

- [x] 역 (Station)
  - 역은 이름을 가지고 있다.
    - [x] 이름은 한글과 숫자만 가능하다.
    - [x] 이름의 길이는 2글자 ~ 9글자만 가능하다.
  - 역 관리
    - [x] 역을 등록할 수 있다.
    - [x] 역을 조회할 수 있다.
    - [x] 역을 삭제할 수 있다.
- [x] 노선 (Line)
  - 노선은 이름과 색상을 가지고 있다.
    - [x] 이름은 한글과 숫자만 가능하다.
    - [x] 이름의 길이는 2글자 ~ 9글자만 가능하다.
    - [x] 색상은 `tailwindcss` 규칙을 따라야 한다. (정규표현식: `^bg-[a-z]{3,7}-\d{2,3}$`)
  - 노선 관리
    - [x] 노선을 등록할 수 있다.
    - [x] 노선을 조회할 수 있다.
      - [x] 단일 노선을 조회할 수 있다.
      - [x] 전체 노선을 조회할 수 있다.
    - [x] 노선을 삭제할 수 있다.
- [x] 구간들 (Sections)
  - 구간들은 역과 역에 대한 구간(Section)을 가지고 있다.
  - 구간 관리
    - [x] 구간을 등록할 수 있다.
      - [x] 노선에 등록된 역이 없을 때, 두 개의 역을 동시에 등록해야 한다.
      - [x] 노선을 갈래길을 가질 수 없으며, 단 방향으로만 연결될 수 있다.
    - [x] 구간을 삭제할 수 있다.
      - [x] 노선에 역이 2개일 때, 하나의 역을 제거하는 경우 모든 구간을 삭제해야 한다
- [x] 구간 (Section)
  - 구간은 연결된 역과 구간 정보를 가지고 있다.
- [x] 구간 정보 (SectionInfo)
  - 구간 정보는 거리와 연결 방향을 가지고 있다.
- [x] 거리 (Distance)
  - 거리는 거리에 대한 값을 가지고 있다.
    - [x] 거리는 1 이상, 100 이하만 가능하다.
- [x] 연결 방향 (Direction)
  - 연결 방향은 하나의 역이 다른 하나의 역과 연결되었을 때, 그 연결 방향(위, 아래)을 가지고 있다
- [x] 간선 (PathSection)
  - 간선은 시작 역, 도착 역, 노선을 가지고 있다.
- [x] 간선들 (PathSections)
  - 간선들은 간선을 가지고 있다.
  - [x] 가지고 있는 간선과 다른 간선이 다른 노선인지 확인할 수 있다.
  - [x] 가지고 있는 간선의 총 합을 계산할 수 있다.
- [x] 최단 거리 계산기 (ShortestPathCalculator)
  - [x] 출발 역과 도착 역을 통해 최단 거리를 계산할 수 있다.
- [x] 요금 정책 (FarePolicy)
  - [x] 이동한 거리에 따라 요금을 계산할 수 있다.

2. 테이블 구조

```sql
CREATE TABLE IF NOT EXISTS line
(
  id BIGINT AUTO_INCREMENT NOT NULL,
  name VARCHAR(255) NOT NULL UNIQUE,
  color VARCHAR(255) NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS station
(
  id BIGINT AUTO_INCREMENT NOT NULL,
  name VARCHAR(255) NOT NULL UNIQUE,
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS section
(
  id BIGINT AUTO_INCREMENT NOT NULL,
  line_id BIGINT NOT NULL,
  up_station_id BIGINT,
  down_station_id BIGINT,
  distance INT NOT NULL,
  PRIMARY KEY(id)
);
```

3. API 문서

- `API 문서`의 경우 [Postman 문서](https://documenter.getpostman.com/view/19879275/2s93eeQpTk) 에서 확인하실 수 있습니다.
