# jwp-subway-path

## API
``station``
``POST /stations`` 역 등록 .

``GET /stations`` 모든 역 정보 조회

``DELETE /stations {id}`` 원하는 역 삭제 .

``/lines``

``POST /lines`` 노선 등록 .

``GET /lines {id}`` 원하는 노선 정보 조회.

``GET /lines`` 노선 전체 조회 .

``DELETE /lines {id}`` 원하는 노선 삭제.

`/line/statios`

`POST /line/{line_id}/stations` 노선의 대한 역 등록.

``DELETE /line/{line_id}/stations`` 노선의 따른 원하는 역 삭제

`GET /line/{line_id}/stations` 노선의 대한 역 조회.

## 기능 목록

- [ ] 노선을 등록 한다.

- [ ] 역을 등록 한다.
  - [ ] 최초 등록 시 두 역을 동시에 등록 한다.
  - [ ] 가운데 등록 시 거리 정보를 고려해야 한다.
    - [ ] 중간에 등록하는 역은 기존의 역과의 거리보다 짧아야 한다.
    
- [ ] 거리정보를 저장한다.
  - [ ] 거리 정보는 양의 정수이다.