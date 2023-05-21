# jwp-subway-shortestPathFinder

# 기능 구현 목록

## step1

### 도메인
- [x] 지하철 노선도 (Subway)
  - [x] 노선 리스트가 있다.
  - [x] 노선을 추가할 수 있다. (입력 : 노선 이름, 노선 색)
  - [x] 노선을 삭제할 수 있다. (입력 : 노선id)
  - [x] 역을 최초 등록한다. (입력 : 노선id, 첫번째 역, 두번째 역, 거리)
  - [x] 역을 추가한다. (입력 : 노선id, 기준 역, 방향, 등록할 역, 거리)
  - [x] 역을 삭제한다. (입력 : 노선id, 삭제할 역)
  - [x] 예외 처리
    - [x] [제한 사항] 이미 존재하는 노선의 이름은 추가할 수 없다.
    - [x] [제한 사항] 이미 존재하는 노선의 색상은 추가할 수 없다.
    - [x] [제한 사항] 존재하지 않는 노선은 삭제할 수 없다.
    - [x] [제한 사항] 역 최초 등록시, 존재하지 않는 노선은 가리킬 수 없다.
    - [x] [제한 사항] 역 추가시, 존재하지 않는 노선은 가리킬 수 없다.
    - [x] [제한 사항] 역 삭제시, 존재하지 않는 노선은 가리킬 수 없다.

- [x] 노선 (Line)
    - [x] 노선은 이름(LineName), 색상(LineColor) 그리고 구간 일급 컬렉션(Sections)이 있다.
    - [x] 예외 처리
      - [x] [제한 사항] 이름 또는 색상은 Null or Empty일 수 없다.

- [x] 구간 일급 컬렉션 (Sections)
  - [x] 구간 리스트가 있다.
  - [x] 예외 처리
    - [x] [제한 사항] 최초 등록 시, 해당 Sections의 Section 개수는 0이어야 한다.
    - [x] [제한 사항] 역 추가 시
      - [x] [제한 사항] 최초 등록이 아니면서 기준역이 존재하지 않으면 추가할 수 없다. 
      - [x] [제한 사항] 추가할 역이 Sections 내에 이미 존재하는 경우 추가할 수 없다.
      - [x] [제한 사항] 중간 추가 시
        - [x] [제한 사항] '추가할 구간의 거리'가 '분리될 기존의 구간 거리'보다 짧아야한다.
    - [x] [제한 사항] 역 삭제 시, 존재하지 않는 역은 삭제할 수 없다.
  - [x] 역을 추가할 수 있다.
    - [x] 최초 등록 시, 두 역을 동시에 등록한다. (최초 등록) (입력 : 등록할 2개의 역, 거리)
    - [x] 최초 등록이 아닐 시 (입력 : 기준 역(firstStation), 방향, 등록할 역, 거리)
      - [x] 가장자리에 역을 추가할 수 있다.
      - [x] 역과 역 사이에 추가 할 수 있다.
        - [x] 역과 역 사이의 거리는 재배정 되어야 한다.
  - [x] 역을 삭제할 수 있다.
    - [x] 노선 내 역이 2개 만 존재하는 경우, 역(구간)이 모두 제거 된다.
    - [x] 가장자리 삭제
    - [x] 중간 역 삭제
      - [x] 다시 이어지는 역의 거리는 합산된다.

- [x] 구간 (Section)
  - [x] 첫번째 역(Station), 두번째 역(Station), 구간 거리(Distance)가 있다.
  - [x] 해당 구간의 왼쪽에 역을 추가할 시, 왼쪽에 추가될 구간을 생성해서 반환한다.
  - [x] 해당 구간의 오른쪽에 역을 추가할 시, 오른쪽에 추가될 구간을 생성해서 반환한다.
  - [x] 해당 구간 사이에 역을 추가할 시, 사이에 추가될 2개의 구간 리스트를 생성해서 반환한다.

- [x] 거리 (Distance)
  - [x] 예외 처리
    - [x] [제한 사항] 거리는 Null 일 수 없습니다.
    - [x] [제한 사항] 거리는 양수여야한다.

- [x] 역 (Station)
  - [x] 이름(StationName)을 가지고 있다.
  - [x] [제한 사항] 이름은 Null or Empty일 수 없습니다.


### 웹 API

- [x] 노선에 역 등록 API
  - [x] 정상 등록
  - [x] 예외 처리
    - [x] [제한 사항] 역 최초 등록시, 존재하지 않는 노선은 가리킬 수 없다.
    - [x] [제한 사항] 역 추가시, 존재하지 않는 노선은 가리킬 수 없다.
    - [x] [제한 사항] 최초 등록 시, 해당 Sections의 Section 개수는 0이어야 한다.
    - [x] [제한 사항] 역 추가 시
      - [x] [제한 사항] 최초 등록이 아니면서 기준역이 존재하지 않으면 추가할 수 없다.
      - [x] [제한 사항] 추가할 역이 Sections 내에 이미 존재하는 경우 추가할 수 없다.
      - [x] [제한 사항] 중간 추가 시
        - [x] [제한 사항] '추가할 구간의 거리'가 '분리될 기존의 구간 거리'보다 짧아야한다.
    - [x] [제한 사항] 역 이름은 Null or Empty일 수 없습니다.
    - [x] [제한 사항] 거리는 Null 일 수 없습니다.
    - [x] [제한 사항] 거리는 양수여야한다.

- [x] 노선에 역 제거 API
  - [x] 정상 제거
  - [x] 예외 처리
    - [x] [제한 사항] 역 삭제 시, 노선에 존재하지 않는 역은 삭제할 수 없다.
    - [x] [제한 사항] 역 삭제 시, 존재하지 않는 노선은 가리킬 수 없다.
    - [x] [제한 사항] lineId가 null일 수 없습니다.
    - [x] [제한 사항] stationId가 null일 수 없습니다.

- [x] 노선 추가하기 API
  - [x] 정상 추가
  - [x] 예외 처리
    - [x] [제한 사항] 이름 또는 색상은 Null or Empty일 수 없다.
    - [x] [제한 사항] 이미 존재하는 노선의 이름은 추가할 수 없다.
    - [x] [제한 사항] 이미 존재하는 노선의 색상은 추가할 수 없다.

- [x] 노선 삭제하기 API
  - [x] 정상 삭제
  - [x] 예외 처리
    - [x] [제한 사항] 존재하지 않는 노선은 삭제할 수 없다.
    - [x] [제한 사항] lineId는 null일 수 없습니다.

- [x] 노선 조회 API (입력 : 노선id)
  - [x] 노선에 포함된 역을 순서대로 보여주도록 응답을 개선합니다.

- [x] 노선 목록 조회 API
  - [x] 노선에 포함된 역을 순서대로 보여주도록 응답을 개선합니다.


## step2
### 도메인
- [x] Path (입력 : 출발 역, 도착 역)
  - [x] 최단 경로, 최단 거리, 요금을 구한다.
  - [x] 예외 처리
    - [x] [제한 사항] 2개의 역은 최소한 '모든 노선 중 하나의 노선'에라도 존재해야한다.
    - [x] [제한 사항] 2개의 역은 이어질 수 있어야 한다.
    - [x] [제한 사항] 2개의 역은 모두 null 또는 empty일 수 없다.
      - Station 테이블이 아닌 '모든 노선' 이라는 것 명심
  - [x] Sections
    - [x] 전달받은 그래프에 자신의 모든 역과 구간, 거리를 추가한다.
  - [x] Section
    - [x] 전달받은 그래프에 자신의 모든 역과, 거리를 추가한다.

- [x] Subway
  - [x] 해당 역이 포함된 모든 노선에서 해당 역을 삭제한다.
  - [x] Line
    - [x] 해당 역을 포함하고 있는지 확인한다.
    - [x] Sections
      - [x] 해당 역을 포함하고 있는지 확인한다.

### 웹 API
- [x] 경로( + 최단 거리, 요금) 조회 API
  - [x] 예외 처리
    - [x] [제한 사항] 2개의 역은 최소한 '모든 노선 중 하나의 노선'에라도 존재해야한다.
    - [x] [제한 사항] 2개의 역은 이어질 수 있어야 한다.
    - [x] [제한 사항] 2개의 역은 모두 null 또는 empty일 수 없다.
- [x] 입력받은 역을 모든 노선에서 삭제 API
  - [x] 정상 입력
  - [x] 예외 처리
    - [x] [제한 사항] 역 삭제 시, 노선에 존재하지 않는 역은 삭제할 수 없다.
    - [x] [제한 사항] stationId가 null일 수 없습니다.
  - [x] http API 문서화


## step3
### 도메인
- [ ] 노선별 추가 요금 정책 반영
  - [ ] Line
    - [ ] 해당 노선의 추가 요금 정보도 가진다. (extraCharge)
    - [ ] 예외 처리
      - [ ] [제한 사항] 추가 요금은 Null일 수 없다.
      - [ ] [제한 사항] 추가 요금은 음수일 수 없다.
  - [ ] Section
    - [ ] 각 구간이 노선의 이름 정보를 가지고 있는다.
  - [ ] ShortestPathFinder
    - [ ] 요금 계산 시, 거쳐간 노선 중 가장 추가 요금이 높은 노선의 추가 요금을 더한다.

- [ ] 연령별 요금 할인 정책 반영
  - [ ] AgeGroup (Enum class)
    - [ ] 각 연령층마다의 요금 계산식이 다르다.
  - [ ] ShortestPathFinder
    - [ ] 연령층 정보(AgeGroup)를 같이 받아서 AgeGroup를 통해 요금을 최종 계산한다.

### API
- [ ] 노선 입력 시, 해당 노선의 추가 요금 정보도 입력하는 것으로 변경
  - [ ] 정상 입력
  - [ ] 예외 처리
    - [ ] [제한 사항] 추가 요금은 Null일 수 없다.
    - [ ] [제한 사항] 추가 요금은 음수일 수 없다.
- [ ] 노선 조회 시, 노선의 추가 요금 정보도 같이 가져오는 것으로 변경
- [ ] 경로 조회 시, 거쳐간 노선 중 가장 추가 요금이 높은 노선의 추가 요금을 더해서 반환하는 것으로 변경
- [ ] 경로 조회 시, AgeGroup(CHILD, TEENAGER, ADULT 셋 중 하나)을 더 입력하는 것으로 변경
  - [ ] [제한 사항] AgeGroup은 Null일 수 없다.
  - [ ] [제한 사항] CHILD, TEENAGER, ADULT 셋 중 하나만 입력할 수 있다.

### DB
- [ ] LINE 테이블에 extra_charge 컬럼 추가
- [ ] SECTION 테이블에 line_id 컬럼 추가
