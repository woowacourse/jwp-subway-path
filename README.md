# jwp-subway-path

---

## 기능 구현 목록

- [x] 테이블 설계
    - [x] station(station_id, name)
    - [x] line(line_id, line_number, color, name)
    - [x] section(section_id, line_id, up_station_id, down_station_id, distance)

- [x] 테스트 작성
    - [x] 통합 테스트로 전체 플로우 테스트
    - [x] TDD 단위 테스트
    - [x] 계층 통합 테스트

- [x] 도메인 로직 구현
    - [x] Graph 구현
    - [x] Route 구현

- [x] 이벤트 구현
  - [x] 성능 개선을 위해서 Route 캐싱처리 및 이벤트 작동시에만 Route(노선맵)을 업데이트 시켜서 잦은 조회 줄임

## API 명세서
[http-request.http](http-request.http)
