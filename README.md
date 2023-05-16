# jwp-subway-path

## 기능 명세

- [x] 노선에 역 등록 API 신규 구현
- [x] 노선에 역 제거 API 신규 구현
- [x] 노선 조회 API 수정
    - 노선에 포함된 역을 순서대로 보여주도록 응답을 개선합니다.
- [x] 노선 목록 조회 API 수정
    - 노선에 포함된 역을 순서대로 보여주도록 응답을 개선합니다.
- [x] 경로 조회 API 구현
    - 출발역, 도착역 사이의 최단 경로 구하기
    - 최단 거리 경로 + 총 거리 정보 응답
    - 여러 노선의 환승도 고려
- [x] 요금 조회 기능 추가
    - 경로 조회 시 요금 정보를 포함하여 응답
    - 거리가 10km 이내: 1250원
    - 10km 초과 50km 이하: 100원/5km
    - 50km 초과: 100원/8km

## API

- [x] 노선에 역 등록
    - POST /lines/{id}/stations
    - Request: {upStationId, downStationId, distance}
    - Response: 201 /lines/{id}
- [x] 노선에 역 제거
    - DELETE /lines/{id}/stations/{station-id}
    - Response: 204
- [x] 최단 경로 조회
    - GET /paths/start/{start-station-id}/end/{end-station-id}
    - Response: 200, {총 거리 정보, 총 요금 정보, {{시작역, 다음역, 거리}, ...}}

## 도메인

- [x] 노선
    - [x] 역을 가질 수 있다
    - [x] 역을 등록할 수 있다
        - 노선에 역이 없을 경우 최초 등록 시 두 역을 등록해야 한다
        - 두 역 가운데 등록할 경우 거리를 고려한다
    - [x] 역 사이에는 거리를 가진다
        - 거리 정보는 양의 정수이다
    - [x] 역을 제거할 수 있다
        - 남은 역이 1개일 경우 같이 제거한다
        - 역이 제거되면 재배치 되어야 한다
