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

- [ ] DELETE '/stations/{stationId}' uri 맵핑
- Request
    - @PathVariable
- Response
    - 제거 Message

### 3. 노선 조회 API

- [ ] GET '/lines/{color}'
    - Request
        - @Pathvariable 노선 색상
    - Response
        - List<StationName>
        - 역 이름은 순서대로 보여준다. (상행종점 -> 하행종점)

### 4. 노선 목록 조회 API

- [ ] GET '/lines'
    - Request X
    - Response
        - Map<Color, List<StationName>>

---

## 비즈니스 로직

### 구현할 기능

- [x] 노선에 역을 등록한다.
    - 노선이 존재하지 않는 경우 상행역과 하행역, 노선, 구간 정보를 저장한다.
    - 노선이 존재하는 경우
        - 두 역 모두 존재하지 않으면 예외가 발생한다.
        - 두 역 모두 존재하면 예외가 발생한다.
        - 두 역 중 하나만 존재하면 존재하지 않는 역과 구간 정보를 저장한다.