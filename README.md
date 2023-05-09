# jwp-subway-path

1. API 기능 요구사항 명세
- [ ] 노선에 역 등록 API 신규 구현
  - [ ] POST `/lines/{lineId}/stations` : 해당 노선에 역을 추가한다.
    - [ ] 하나의 역을 등록할 때 : 노선 id, 등록할 역 id, 이전역 id, 다음역 id, 이전역과의 거리 정보, 다음역과의 거리 정보
    - [ ] 처음 노선에 두개의 역을 등록할 때 : 노선 id, 첫번째 역 id, 두번째 역 id, 역 사이 거리정보
- [ ] 노선에 역 제거 API 신규 구현
  - [ ]  DELETE `/lines/{lineId}/stations` : 해당 노선에서 역을 제거한다.
    - [ ] 하나의 역을 제거할 때 : 노선 id, 노선-역 테이블의 id
    - [ ] 노선에 남은 마지막 두개의 역을 제거할 때: 노선 id, 노선-역 테이블의 첫번째 id, 노선-역 테이블의 두번째 id
- [ ] 노선 조회 API 수정
  - [ ] 노선에 포함된 역을 순서대로 보여주도록 응답을 개선한다.
    - [ ] 역을 추가할 때는 이전역 다음역 정보를 같이 저장함으로써 순서를 표시한다.
    - [ ] 이전역, 다음역 정보를 이용해서 도메인을 순서에 맞게 조립한다.
- [ ] 노선 목록 조회 API 수정
  - [ ] 노선에 포함된 역을 순서대로 보여주도록 응답을 개선한다.

2. 도메인 기능 명세

- 역들 (Stations)
  - [ ] 여러 역(station)에 대한 정보를 갖는다.
  - [ ] 입력된 역이 존재하는 역인지 확인할 수 있다.
  - [ ] 새로운 역을 추가할 수 있다.
  - [ ] 역을 삭제할 수 있다.
- 역(Station)
  - [ ] 역은 id, 이름을 갖는다.
  - [ ] 역 이름은 중복일 수 없다.
- 노선(Line)
 - [ ] 노선은 id, 이름, 정거장들의 정보를 갖는다.
 - [ ] 노선 이름은 중복일 수 없다.
 - [ ] 동일한 구간을 중복으로 가질 수 없다.
 - [ ] 노선에 처음 구간이 추가될 때는 `null - 상행 종점`, `상행 종점 - 하행 종점`, `하행 종점 - null` 세 구간이 추가된다.
 - [ ] 노선에 역이 추가될 때는 `상행 방향 역 - 하행 방향 역`구간이 `상행 방향 역 - 새로운 역`과 `새로운 역 - 하행 방향 역`으로 나뉜다.
   - [ ] 추가되는 역과 상행 방향 역, 하행 방향 역 사이의 거리가 각 구간에 저장되어야 한다.
 - [ ] 노선에서 역이 제거될 때는 `상행 방향 역 - 제거할 역`과 `제거할 역 - 하행 방향 역` 두 구간이 `상행 방향 역 - 하행 방향 역`으로 합쳐진다.
   - [ ] 제거되는 역과 상행 방향 역, 하행 방향 역 사이의 거리의 합이 합쳐진 구간에 저장되어야 한다.
 - [ ] 노선에 남은 마지막 역 두개를 제거할 때는 `null - 상행 종점`, `상행 종점 - 하행 종점`, `하행 종점 - null` 세 구간이 삭제된다.
   - [ ] 제거 처리 이후 구간이 두개만 남은 경우, 모든 구간을 삭제한다.
- 구간(Section)
  - [ ] 구간은 id, 상행 방향 역(Station) 정보, 하행 방향 역(Station) 정보, 역 사이 거리를 갖는다.
- 예외 처리
  - [ ] 구간 사이에 역이 추가될 때, `구간 내 두 역과 새로운 역 사이 거리 각각의 합 != 구간의 기존 거리` 인 경우 예외처리한다.
  - [ ] 거리 정보가 양수가 아닐 경우 예외처리한다.
