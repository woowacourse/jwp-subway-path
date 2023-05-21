# 구현 기능 목록

- [x] 노선에 역 등록 API
  - [x] 노선에 역이 등록될 때 거리 정보도 함께 포함되어야 한다.
  - [x] 노선에 역이 없을 경우 두 역을 동시에 등록해야 한다.
  - [x] 노선은 갈래길을 가질 수 없다.

- [x] 노선에 역 제거 API
  - [x] 노선에서 역이 제거될 경우 역과 역 사이의 거리도 **재배정**되어야 한다.
  - [x] 노선에 등록된 역이 2개 인 경우 하나의 역을 제거할 때 두 역이 모두 제거되어야 한다.

- [x] 노선 조회 API 수정
  - [x] 노선에 포함된 역을 순서대로 보여준다.