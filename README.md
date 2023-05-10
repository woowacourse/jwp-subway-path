# jwp-subway-path

## 도메인

- 노선에 역 등록
    - 노선은 이름을 갖는다
    - 노선에는 역을 자유롭게 등록할 수 있다
    - 노선에 역을 등록할 때 역 사이의 거리 정보를 저장해야 한다
    - 거리 정보는 양의 정수
    - 노선에 역을 최초로 등록할 때는 반드시 두 역을 동시에 등록
    - 하나의 역은 여러 노선에 등록 가능
    - 노선은 갈래길을 가질 수 없음 (ex. a-b-c에서 b-c사이에 d 추가시 반드시 a-b-d-c)
    - 노선 중간에 역이 등록되면 거리 정보를 고려해야 함

- 노선에 역 제거
    - 노선에서 역 제거시 재배치
    - 노선에서 역 제거시 거리 재배정
    - 노선에 역이 두개만 있는 경우 하나의 역이 제거되면 두 역 모두 제거

## API 명세

- [x] Line(노선)
    - [x] 노선 전체 조회
        - GET /lines
        - Response: Status 200, {{id, 노선이름, 색}, ...}
    - [x] 노선 추가
        - POST /lines
        - Request: {노선이름, 색}
        - Response: Status 201, Location: /lines/{id}
    - [x] 해당 노선 역 전체 조회
        - GET /lines/{id}/stations
        - Response: Status 200, {{id, 이름}, ...}
    - [ ] 노선에 역 등록
        - POST /lines/{id}/paths
        - Request: {line_id, 상행역 이름, 하행역 이름, 거리}
        - Response: Status 201, Locations: /lines/{id}/stations/{id}
    - [ ] 노선에 역 제거
        - DELETE /lines/{id}/stations/{id}
        - Response: Status 204