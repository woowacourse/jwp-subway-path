# jwp-subway-path

# 구현할 기능 목록

- 노선에 역 등록 API 신규 구현
    - 두 개 추가
        - POST "/lines/{lineId}/stations"
          ```json
              {
                "topId": 1,
                "bottomId": 2,
                "distance": 10
              }
            ```
    - 한 개 추가
        - POST "/lines/{lineId}/station"
          ```json
              {
                "where": "UPPER",
                "stationId": 1,
                "baseId": 2,
                "distance": 10
              }
          ```
          ```json
              {
                "where": "LOWER",
                "stationId": 1,
                "baseId": 2,
                "distance": 10
              }
          ```

        - 응답 {Status Code: `OK`}
- 노선에 역 제거 API 신규 구현
    - DELETE "/lines/{lineId}/station
        ```json
              {
                "stationId": 1
              }
        ```
    - 응답 {Status Code: `NO_CONTENT`}

![img.png](잠실인근노선.png)

- 도메인
    - 노선
        - 역을 등록한다
            - 노선에 등록되는 역의 위치는 자유롭게 지정할 수 있다.
                - 역과 역 사이에 역을 등록할 수 있다.
                - 종점의 앞이나 뒤에도 역을 등록할 수 있다.
            - 노선에 역이 등록될 때 거리 정보도 함께 포함되어야 한다.
                - 인접한 역들과의 거리 정보가 저장되어야 한다.
                - 거리 정보는 양의 정수로 제한한다.
            - 노선에 역이 하나도 등록되지 않은 상황에서 최초 등록 시 두 역을 동시에 등록해야 한다.
            - 노선은 갈래길을 가질 수 없다.
            - 노선 가운데 역이 등록될 경우 거리 정보를 고려해야 한다.
                - 역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록할 수 없다.
        - 역을 제거한다
            - 노선에서 역을 제거할 경우 정상 동작을 위해 재배치 되어야 한다.
            - 노선에서 역이 제거될 경우 역과 역 사이의 거리도 재배정되어야 한다.
            - 노선에 등록된 역이 2개인 경우 하나의 역을 제거할 때 두 역이 모두 제거되어야 한다.
    - 역
        - 이름이 같으면 같은 역으로 취급한다
