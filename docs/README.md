# 기능 목록


## API 설계

### 역 추가

- Request
  - POST /lines/{lineId}/stations 
      - stationName
      - upstreamName(상행 종점으로 역을 추가하고 싶으면 ""을 넣는다)
      - downstreamName(하행 종점으로 역을 추가하고 싶으면 ""을 넣는다)
      - distanceToUpstream(단, 상행 종점으로 추가되는 경우에는 하행역과의 거리를 넣는다.)

- Response
  - 400 BAD REQUEST
    - (stationName < 2 || stationName > 15)
    - (upstreamName, downstreamName이 Section으로 존재하지 않는 경우)
    - (distanceToUpstream >= distance(upstream, downstream)인 경우)
    - (stationName이 이미 Line에 존재하는 경우)
    - (lineId에 해당하는 Line이 존재하지 않는 경우)
  - 201 CREATED

### 노선 추가

- Request
  - POST /lines
      - lineName
      - upstreamName
      - downstreamName
      - distance

- Response
  - 400 BAD REQUEST
    - (upstreamName < 2 || upstreamName > 15) 
    - (downstreamName < 2 || downstreamName > 15) 
    - (lineName < 2 || lineName > 15)
    - (distance < 1인 경우)
    - (lineName의 노선이 이미 존재하는 경우)
  - 201 CREATED

### 역 삭제 

- Request
  - DELETE /lines/{lineId}/stations/{stationId}

- Response
  - 400 BAD REQUEST
    - (lineId가 존재하지 않는 경우)
    - (stationId가 Line에 존재하지 않는 경우)
  - 204 NO-CONTENT

### 노선 조회

- Request
  - GET /lines/{lineId}

- Response
  - 400 BAD REQUEST
    - (lineId가 존재하지 않는 경우)
  - 200 OK

### 모든 노선 조회

- Request
  - GET /lines

- Response
  - 200 OK



## 도메인

- [x] Station
  - [x] 역 이름을 갖는다.
    - [x] 역 이름이 2 이상 15글자 이하가 아닌 경우 예외를 던진다.

- [x] Section
  - [x] 하행역, 상행역에 해당하는 Station을 갖는다.
  - [x] 구간의 거리를 갖는다.
    - [x] 구간의 거리가 양의 정수가 아닌 경우 예외를 던진다.
  - [x] upstreamName, downstreamName이 일치하는지 확인한다.
  - [x] Station을 중간에 추가해서 새로운 Section들을 반환한다.
  - [x] 두 Section을 병합한다.
    - [x] 하나의 Downstream과 다른 하나의 Upstream이 같지 않은 경우 예외를 던진다.
 
- [x] Line
  - [x] 이름을 갖는다.
    - [x] 이름이 2자 이상 15자 이하가 아니면 예외를 던진다.
  - [x] Line에 해당하는 Section들을 갖는다 .
  - [x] Station을 추가할 수 있다.
    - [x] 추가하려는 Station이 이미 존재하는 경우 예외를 던진다.
    - [x] Station을 추가할 때 Upstream과 Downstream이 Section으로 등록되지 않은 경우 예외를 던진다.
  - [x] Station을 삭제할 수 있다.
    - [x] Line에 존재하지 않는 Station을 삭제할 경우 예외를 던진다.
