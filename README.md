# jwp-subway-path

## 기능 명세

- [ ] 노선에 역 등록 API 신규 구현
- [ ] 노선에 역 제거 API 신규 구현
- [ ] 노선 조회 API 수정
    - 노선에 포함된 역을 순서대로 보여주도록 응답을 개선합니다.
- [ ] 노선 목록 조회 API 수정
    - 노선에 포함된 역을 순서대로 보여주도록 응답을 개선합니다.

## API

- [ ] 노선에 역 등록
    - POST /lines/{id}/stations
    - Request: {upStationId, downStationId, distance}
    - Response: 201 /lines/{id}/stations/{station-id}
- [ ] 노선에 역 제거
    - DELETE /lines/{id}/stations/{station-id}
    - Response: 204

## 도메인

- [ ] 노선
    - [ ] 역을 가질 수 있다
    - [ ] 역을 등록할 수 있다
        - 노선에 역이 없을 경우 최초 등록 시 두 역을 등록해야 한다
        - 두 역 가운데 등록할 경우 거리를 고려한다
    - [ ] 역 사이에는 거리를 가진다
        - 거리 정보는 양의 정수이다
    - [ ] 역을 제거할 수 있다
        - 남은 역이 1개일 경우 같이 제거한다
        - 역이 제거되면 재배치 되어야 한다
