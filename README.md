# jwp-subway-path

## API

### 1. 노선 역 등록 API

- [x] POST '/stations' uri 맵핑
    - Request
        - 상행 역 이름
        - 하행 역 이름
        - 두 역 사이의 거리
        - 등록할 호선 색상
    - Response X

### 2. 노선 역 제거 API

- [x] DELETE '/stations/{stationId}' uri 맵핑
- Request
    - @PathVariable {stationId}
- Response X

### 3. 노선 조회 API

- [x] GET '/lines/{lineId}'
    - Request
        - @Pathvariable {lineId}
    - Response
        - List<StationName>
        - 역 이름은 순서대로 보여준다. (상행종점 -> 하행종점)

### 4. 노선 목록 조회 API

- [x] GET '/lines'
    - Request X
    - Response
        - List<List<StationName>>
        - 각 노선의 역 이름은 순서대로 보여준다. (상행종점 -> 하행종점))

### 5.경로 조회 API 구현

- [x] GET '/path'
    - Request
        - 상행역 id
        - 하행역 id
    - Response
        - 경로 상에 있는 역 이름
        - 거리
        - 요금

---

## 비즈니스 로직

### 구현할 기능

- [x] 노선에 역을 등록한다.
    - 노선이 존재하지 않는 경우 상행역과 하행역, 노선, 구간 정보를 저장한다.
    - 노선이 존재하는 경우
        - 두 역 모두 존재하지 않으면 예외가 발생한다.
        - 두 역 모두 존재하면 예외가 발생한다.
        - 두 역 중 하나만 존재하는 경우
            - 가운데 역을 등록할 때 존재하지 않는 역과 구간 정보를 저장한다.
            - 종점을 등록할 때 존재하지 않는 역과 구간 정보를 저장한다.

- [x] 노선에서 역을 제거한다.
    - 해당하는 역이 없으면 예외가 발생한다.
    - 노선에 등록된 역이 2개인 경우 하나의 역을 제거할 때 두 역을 모두 제거한다.
    - 해당하는 역이 종점인 경우
        - 해당 역과 구간을 삭제한다.
    - 해당하는 역이 가운데 역인 경우
        - 기존 역의 양 옆에 위치한 역을 연결한다.
            - 거리가 재배정된다.

- [x] 노선 하나의 모든 역을 순서에 맞게 조회한다.
- [x] 모든 노선의 모든 역을 순서에 맞게 조회한다.

- [x] 경로 조회 시 출발역과 도착역 사이의 최단 거리 경로를 구한다.
- [x] 경로 조회 시 출발역과 도착역 사이의 최단 거리를 구한다.
- [x] 거리를 바탕으로 요금을 계산한다.
    - 기본운임(10㎞ 이내): 기본운임 1,250원
    - 11km~50km: 5km 까지 마다 100원 추가
    - 50km 초과: 8km 까지 마다 100원 추가

---

## DB

![지하철 ERD](https://github.com/woowacourse-precourse/java-menu/assets/96688810/d33cfc2a-1fe9-4eb5-852d-1e586bffef8e)
