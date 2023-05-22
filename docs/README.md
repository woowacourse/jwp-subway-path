# 기능 목록

## API 설계

---

## 1단계 요구사항

### 역 추가

- Request
  - POST /line/stations 
      - station-name
      - line-name
      - upstream-name(상행 종점으로 역을 추가하고 싶으면 ""을 넣는다)
      - downstream-name(하행 종점으로 역을 추가하고 싶으면 ""을 넣는다)
      - distance-to-upstream

- Response
  - 400 BAD REQUEST
    - (station-name < 2 || station-name > 15) ->
    - (line-name < 2 || line-name > 15) ->
    - (line-name이 존재하지 않는 경우)
    - (upstream-name, downstream-name이 Section으로 존재하지 않는 경우)
    - (distance-to-upstream >= distance(upstream, downstream)인 경우)
    - (station-name이 이미 Line에 존재하는 경우)
  - 201 CREATED

- Request
  - POST /lines
      - line-name 
      - upstream-name
      - downstream-name
      - distance

- Response
  - 400 BAD REQUEST
    - (upstream-name < 2 || upstream-name > 15) ->
    - (downstream-name < 2 || downstream-name > 15) ->
    - (line-name < 2 || line-name > 15) ->
    - (distance < 1인 경우)
    - (line-name의 노선이 이미 존재하는 경우)
  - 201 CREATED

- Request
  - DELETE /line/stations
      - line-name
      - station-name

- Response
  - 400 BAD REQUEST
    - (line-name이 존재하지 않는 경우)
    - (station-name이 Line에 존재하지 않는 경우)
  - 204 NO-CONTENT

### 노선 조회

- Request
  - GET /lines/{lineId}
      - Path Variable: 노선 아이디

- Response
  - 400 BAD REQUEST
    - (line-id가 존재하지 않는 경우)
  - 200 OK

- Request
  - GET /lines

- Response
  - 200 OK

---

## 2단계 요구사항

### 경로 조회, 요금 조회

- Request
  - Get /lines/paths?start={startStationId}&destination={destinationId}
    - Query String: 출발역 아이디, 도착역 아이디

- Response
  - 400 BAD REQUEST
    - (출발역과 도착역이 연결되어 있지 않은 경우)
    - (출발역이 존재하지 않는 경우)
    - (도착역이 존재하지 않는 경우)
    - (출발역과 도착역 동일한 경우)
    
  - 200 OK
    - 응답 데이터
      - 최단 거리 경로 정보
      - 총 거리 정보
      - 요금 정보

---

## 도메인

- [x] Station
  - [x] 역 이름을 갖는다.
    - [x] 역 이름이 2 이상 15글자 이하가 아닌 경우 예외를 던진다.

- [x] Section
  - [x] 하행역, 상행역에 해당하는 Station을 갖는다.
  - [x] 구간의 거리를 갖는다.
    - [x] 구간의 거리가 양의 정수가 아닌 경우 예외를 던진다.
  - [x] upstream-name, downstream-name이 일치하는지 확인한다.
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

- [x] Subway
  - [x] Line들을 갖는다.
  - [x] 최단 경로를 찾는다.
    - 예외 상황
      - [x] 출발역이 존재하지 않는 경우 예외를 던진다.
      - [x] 도착역이 존재하지 않는 경우 예외를 던진다.
      - [ ] 출발역과 도착역이 연결되어있지 않은 경우 예외를 던진다.
      - [ ] 출발역과 도착역 동일한 경우 예외를 던진다.
    - [x] 총 거리 정보를 계산한다.
    - [ ] 요금 정보를 계산한다.
