# jwp-subway-path

## API

### 1. 노선 역 등록 API
- [ ] POST '/stations' uri 맵핑
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
- 구현할 기능
  - 등록할 역 이름과 다음 역 이름을 항상 모두 입력해야 한다.
    - 노선에 역이 하나도 등록되지 않은 상황에서 최초 등록 시 두 역이 동시에 등록된다.
  - 상행일 때는 요청 받은 다음 역이 Section 도메인에서 upStation이 된다.
    - 등록한 역은 downStation이 된다.
  - 하행일 때는 요청 받은 다음 역이 Section 도메인에서 downStation이 된다.
    - 등록한 역은 upStation이 된다.
  - 등록할 역 이름으로 Station을 생성한다.
  - Stations 에 등록할 역을 추가한다.
    - Stations는 노선에 포함된 역이 순서대로 저장되어 있다.
    - Stations는 LinkedList<Station> 을 갖는다.
  - 등록할 역 이름과 호선 색깔을 line 테이블에 추가한다.