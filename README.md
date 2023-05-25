# 프로그램 개요
지하철 노선도를 관리하는 프로그램입니다.
노선과 역을 추가, 제거할 수 있습니다.
## 용어 설명
- 역 : 노선을 따라 지정된 지점
- 노선 : 여러 역이 연결된 경로
- 구간 : 역과 역 사이


# 프로그램 설계

### 클래스 다이어그램 - MVC
```mermaid
classDiagram
    WebEnvironment <--> LineController
    LineController --> LineService
    LineService --> LineRepository
    LineRepository <|.. DbLineRepository

    WebEnvironment <--> TransferController
    TransferController --> TransferService
    TransferService --> LineService
    TransferService  --> TransferRepository
    TransferRepository <|.. DbTransferRepository
    
    WebEnvironment <--> PathController
    PathController --> SubwayMapService
    SubwayMapService --> LineRepository
    SubwayMapService  --> TransferRepository
```

### 클래스 다이어그램 - 도메인
```mermaid
classDiagram
    JgraphtSubwayMap --> Line
    JgraphtSubwayMap --> Transfer
    Line *-- Section
    Section *-- Station
    Transfer *-- Station
    class Line {
        - List(Section) sections
    }
    class Section {
        - Station upwardStation
        - Station downwardStation
        - int distance
    }
    class Station {
        - String name
    }
    class Transfer {
        - Station firstStation
        - Station lastStation
    }
    class JgraphtSubwayMap{
        - ShortestPathAlgorithm pathAlgorithm
    }
```

### Entity-Relationship Diagram
```mermaid
erDiagram
  LINE ||--|{ SECTION : ""
  LINE ||--|{ STATION : ""
  LINE {
		BIGINT id PK
		VARCHAR(50) name
        BIGINT surcharge
        DATE created_at
	}
    SECTION {
        BIGINT id PK
        BIGINT line_id FK
        BIGINT upward_station_id
        BIGINT downward_station_id
        INT distance
        DATE created_at
    }
    STATION {
        BIGINT id PK
        BIGINT line_id FK
        VARCHAR(255) name
        DATE created_at
    }
    TRANSFER {
        BIGINT id PK
        BIGINT first_station_id
        BIGINT last_station_id
    }
```

# API 명세서
Swagger link : http://localhost:8080/swagger-ui/index.html#/
(SubwayApplication 실행 후 접속)


# 기능 목록

### Line
- [x] 노선을 생성한다.
  - [x] 구간을 생성한다.
- [x] 노선에 역을 추가한다.
  - [x] 예외) 이미 역이 존재하는 경우
  - [x] 예외) 이웃역이 존재하지 않을 경우
  - [x] 역이 위치할 곳의 구간을 생성한다.
    - [x] 예외) 새로운 구간의 거리가 기존 구간의 거리보다 크거나 같은 경우
- [x] 노선에서 역을 제거한다.
  - [x] 예외) 노선에 존재하지 않는 역인 경우
  - [x] 예외) 노선에 2개의 역만 존재하는 경우
  - [x] 구간을 새로 생성한다.
    - [x] 제거한 역이 포함된 구간을 노선에서 삭제한다.
      - [x] 역이 종점인지 아닌지 판단한다.
        - [x] 제거한 역이 종점역이 아닌 경우, 이웃하고 있던 두 역이 포함된 구간을 노선에 추가한다.

### Section
- [x] 상행역, 하행역과 거리를 갖는다.
- [x] 남은 거리를 계산한다.
- [x] 특정 역의 포함 여부를 반환한다.
- [x] 상행역인지 여부를 반환한다.
- [x] 하행역인지 여부를 반환한다.

### Station
- [x] 역의 이름을 갖는다.

### Transfer
- [x] 환승 가능한 2개의 역을 갖는다.
  - [x] ID가 빠른 순서로 저장한다.
  - [x] 같은 역을 가질 수 없다.

### SubwayMap
- [x] 최단 거리 경로를 계산한다.
  - [x] 경로에 있는 모든 역을 노선 기준으로 나누어 반환한다. 
  - [x] 경로에 알맞은 요금을 계산한다.
    - [x] 기본운임(10㎞ 이내): 기본운임 1,250원
    - [x] 10km~50km: 5km 까지 마다 100원 추가
    - [x] 50km 초과: 8km 까지 마다 100원 추가
