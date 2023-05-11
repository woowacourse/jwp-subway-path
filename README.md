# jwp-subway-path

### Table
- [ ] station
    - [ ] id(PK)
    - [ ] name(UNIQUE)
- [ ] line
    - [ ] id(PK)
    - [ ] name(UNIQUE)
    - [ ] color(UNIQUE)
- [ ] line_station
    - [ ] id(PK)
    - [ ] up_bound_id REFERENCE station
    - [ ] down_bound_id REFERENCE station
    - [ ] line_id REFERENCE line
    - [ ] distance

### API
- [ ] 지하철
  - [ ] 단건조회 GET "/stations/{id}" 
  - [ ] 전체조회 GET "/stations" 
  - [ ] 등록 POST "/stations"
  - [ ] 수정 PUT "/stations/{id}"
  - [ ] 삭제 DELETE "/stations/{id}"
- [ ] 노선
  - [ ] 단건조회 GET "/lines/{id}" -> 특정 lines에 해당하는 전체 지하철역 반환 
  - [ ] 전체조회 GET "/lines" -> 모든 lines에 해당하는 전체 지하철역 반환
  - [ ] 등록 POST "/lines"
  - [ ] 수정 PUT "/lines/{id}"
  - [ ] 삭제 DELETE "/lines/{id}"
- [ ] 지하철 + 노선 (노선에 역)
  - [ ] 등록 POST "/lines/{line_id}/stations/"
  - [ ] 제거 DELETE "/lines/{line_id}"/stations/{station_id}


### 도메인
- [ ] Line: 노선
- [ ] Station: 지하철역
- [ ] Section: 역 + 연결(거리) + 역
- [ ] Sections: Section 여러 개
- [ ] LineStation: 특정 노선에 해당하는 Section 저장
