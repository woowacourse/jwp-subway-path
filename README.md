# jwp-subway-path

# 기능 구현 목록

## step1

### 도메인
- [ ] 지하철 노선도 (SubwayMap)
  - [ ] 노선 리스트가 있다.
  - [ ] 노선을 추가할 수 있다.
  - [ ] 노선을 삭제할 수 있다.
  - [ ] 역을 추가한다. (입력 : 노선 이름, 기준 역, 방향, 등록할 역, 거리)

- [ ] 노선 (Line)
    - [ ] 노선은 이름(LineName), 색상(LineColor) 그리고 구간 일급 컬렉션(Sections)이 있다.
    - [ ] 예외 처리
      - [ ] [제한 사항] 이름 또는 색상은 Null or Empty일 수 없다.
      - [ ] [제한 사항] 노선의 이름은 "선"으로 끝나야 한다.
      - [ ] [제한 사항] 이미 존재하는 이름은 추가할 수 없다.
      - [ ] [제한 사항] 이미 존재하는 색상은 추가할 수 없다.

- [ ] 구간 일급 컬렉션 (Sections)
  - [x] 구간 리스트가 있다.
  - [ ] 예외 처리
    - [x] [제한 사항] 최초 등록 시, 해당 Sections의 Section 개수는 0이어야 한다.
    - [ ] [제한 사항] 역 추가 시
      - [x] [제한 사항] 최초 등록이 아니면서 기준역이 존재하지 않으면 추가할 수 없다. 
      - [x] [제한 사항] 추가할 역이 Sections 내에 이미 존재하는 경우 추가할 수 없다.
      - [ ] [제한 사항] 중간 추가 시
        - [ ] [제한 사항] '추가할 구간의 거리'가 '분리될 기존의 구간 거리'보다 짧아야한다.
        - [ ] [제한 사항] 분리될 구간의 길이가 2 이상이어야 한다.
    - [ ] [제한 사항] 역 삭제 시, 존재하지 않는 역은 삭제할 수 없다.
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



### 서비스
- [ ] StationService
  - [ ] 역 추가 시, 이미 DB(Station)에 존재하는 역이면, 바로 해당 노선에 추가한다.
  - [ ] 역 추가 시, DB(Station)에 존재하지 않는 역이면, DB에 추가 후 해당 노선에 추가한다.


### 웹 API

- [ ] 노선에 역 등록 API
- [ ] 노선에 역 제거 API
- [ ] 노선 조회 API
- [ ] 노선 목록 API
- [ ] [제한 사항] 각 API에 포함되는 모든 예외처리 테스트 (컨트롤러, 서비스, 도메인, Repository)
