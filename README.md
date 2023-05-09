# jwp-subway-path

---

## 기능 구현 목록

- [x] 테이블 설계
    - [x] station(station_id, name)
    - [x] line(line_id, color, name)
    - [x] section(section_id, sectionA_id, sectionB_id)
    - [x] station_in_line(station_point_id, section_id, line_id)
    - [x] station_end_point (id, upStation_id, downStation_id)

- [ ] 테스트 작성
  - [ ] 통합 테스트로 전체 플로우 테스트
  - [ ] TDD 단위 테스트
  - [ ] 계층 통합 테스트

- [ ] 도메인 로직 구현
  - [ ] Graph 구현


## API 명세서
