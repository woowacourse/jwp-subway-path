# jwp-subway-path

### API

#### /stations
- [ ] 역 등록
  - [ ] POST, /stations
  - [ ] 요청 포맷
    - [ ] 역 이름
  - [ ] 응답 포맷
    - [ ] Locations : /stations/{id}
- [ ] 역 제거
  - [ ] DELETE, /stations/{id}
  - [ ] 요청 포맷
    - [ ] 역 id

#### /lines
- [ ] 노선 등록
  - [ ] POST, /lines
  - [ ] 요청 포맷
    - [ ] 노선 이름, 노선 색깔
  - [ ] 응답 포맷
    - [ ] Locations : /lines/{id}
- [ ] 조회
  - [ ] GET, /lines
  - [ ] 응답
    - [ ] List<List<Station>> 
- [ ] 노선별 조회
  - [ ] GET, /lines?id=

#### /lines/{line_id}/station
- [ ] 노선에 역 등록
  - [ ] POST, /lines/{line_id}/stations/
  - [ ] 요청 포맷
    - [ ] 상행 역 id, 하행 역 id, 거리 
- [ ] 노선에서 역 삭제
  - [ ] DELETE, /lines/{line_id}/stations/{station_id}

### Table
- [ ] station
  - [ ] id(PK)
  - [ ] name(UNIQUE)
- [ ] line
  - [ ] id(PK)
  - [ ] name(UNIQUE)
  - [ ] color(UNIQUE)
- [ ] section
  - [ ] id(PK)
  - [ ] line_id REFERENCE line
  - [ ] source_station_id REFERENCE station
  - [ ] target_station_id REFERENCE station
  - [ ] distance

### Domain
- [ ] Station
  - [ ] name
  - [ ] line 
- [ ] Line
  - [ ] name
  - [ ] color
- [ ] Section
  - [ ] Target Station
  - [ ] direction
  - [ ] distance
- [ ] Subway
  - [ ] Map<Station, List<Section>>

### 기능 요구사항

#### 노선에 역 등록
- [ ] 요청 시 거리는 양의 정수여야 한다

- [ ] section 테이블에 요청된 노선에 해당하는 구간이 있는지 확인한다
  - [ ] 없으면 두 역 다 등록한다
  - [ ] 노선에 해당하는 구간이 있으면
    - [ ] source, target 역이 이미 해당 노선에 등록되어 있으면 예외 발생
    - [ ] source, target 역이 해당 노선에 둘 다 등록되어 있지 않으면 예외 발생
    - [ ] 노선에 source 역이 등록되어 있을 때
      - [ ] section 테이블에서 기존 source 역의 target 역과의 거리를 구한다
      - [ ] 요청 거리가 위에서 구한 거리 이상이면 예외 발생
      - [ ] 요청된 target 역 추가 후 거리 재배치
    - [ ] 노선에 target 역이 등록되어 있을 때
      - [ ] section 테이블에서 기존 target 역의 source 역과의 거리를 구한다
      - [ ] 요청 거리가 위에서 구한 거리 이상이면 예외 발생
      - [ ] 요청된 source 역 추가 후 거리 재배치

#### 노선에 역 제거
- [ ] A-B-C-D 역이 있는 노선에서 C역이 제거되는 경우 A-B-D 순으로 재배치된다
- [ ] 역과 역 사이의 거리도 재배정되어야 한다
- [ ] 노선에 등록된 역이 2개 인 경우 하나의 역을 제거할 때 두 역이 모두 제거되어야 한다
