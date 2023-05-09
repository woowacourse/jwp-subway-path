# jwp-subway-path

# 구현할 기능 목록

- [ ] 노선에 역 등록 API 신규 구현
    - [ ] 요청 {HTTP method: POST, URI: "/lines/{lineId}/station", body: stationId의 리스트}
        - ```json
        [
          {
            "id": 1
          },
          {
            "id": 2
          }
        ]
        ```


- [ ] 응답 {Status Code: OK}
- [ ] 노선에 역 제거 API 신규 구현
    - [ ] 요청 {HTTP method: DELETE, URI: "/lines/{lineId}/station}", body stationIds}
        - ```json
        [
          {
            "id": 1
          },
          ...
          {
            "id": 2
          }
        ]
        ```
