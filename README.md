# jwp-subway-path

### Table
- [x] station
    - [x] id(PK)
    - [x] name(UNIQUE)
- [x] line
    - [x] id(PK)
    - [x] name(UNIQUE)
    - [x] color(UNIQUE)
- [x] line_station
    - [x] id(PK)
    - [x] up_bound_id REFERENCE station
    - [x] down_bound_id REFERENCE station
    - [x] line_id REFERENCE line
    - [x] distance

### API
- [x] 지하철
    - [x] 단건조회 GET "/stations/{id}"
    - [x] 전체조회 GET "/stations"
    - [x] 등록 POST "/stations"
    - [x] 수정 PUT "/stations/{id}"
    - [x] 삭제 DELETE "/stations/{id}"
- [x] 노선
    - [x] 단건조회 GET "/lines/{id}" -> 특정 lines에 해당하는 전체 지하철역 반환
    - [x] 전체조회 GET "/lines" -> 모든 lines에 해당하는 전체 지하철역 반환
    - [x] 등록 POST "/lines"
    - [x] 수정 PUT "/lines/{id}"
    - [x] 삭제 DELETE "/lines/{id}"
- [x] 지하철 + 노선 (노선에 역)
    - [x] 등록 POST "/lines/{line_id}/stations/"
        - RequestBody "upBoundStationId" : ~
          "downBoundStationId" : ~
          "distance" : ~
    - [x] 제거 DELETE "/lines/{line_id}"/stations/{station_id}
- [x] 경로조회와 요금계산
    - [x] 경로조회와 요금계산 POST "/paths"


### 도메인
- [x] Line: 노선
- [x] Station: 지하철역
- [x] Section: 역 + 연결(거리) + 역
- [x] Sections: Section 여러 개
- [x] LineStation: 특정 노선에 해당하는 Section 저장
- [x] Path: 최단거리
- [x] FeePolicy: 요금 계산
