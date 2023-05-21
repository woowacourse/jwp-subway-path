# jwp-subway-path

### 역 관련 기능

- Station 등록 API - POST /stations
  - [x] 통합 테스트 작성
  - [x] Dao 단위 테스트 작성
  - [x] Dao 프로덕션 코드 작성
  - [x] Service 단위 테스트
  - [x] Service 프로덕션 코드 작성
  - [x] Controller 단위 테스트
  - [x] Controller 프로덕션 코드 작성


- Station 전체 조회 API - GET /stations
  - [x] 통합 테스트 작성
  - [x] Dao 단위 테스트 작성
  - [x] Dao 프로덕션 코드 작성
  - [x] Service 단위 테스트
  - [x] Service 프로덕션 코드 작성
  - [x] Controller 단위 테스트
  - [x] Controller 프로덕션 코드 작성


- Station id로 조회 API - GET /stations/{id}
  - [x] 통합 테스트 작성
  - [x] Dao 단위 테스트 작성
  - [x] Dao 프로덕션 코드 작성
  - [x] Service 단위 테스트
  - [x] Service 프로덕션 코드 작성
  - [x] Controller 단위 테스트
  - [x] Controller 프로덕션 코드 작성


- Station 수정 API - PUT /stations/{id}
  - [x] 통합 테스트 작성
  - [x] Dao 단위 테스트 작성
  - [x] Dao 프로덕션 코드 작성
  - [x] Service 단위 테스트
  - [x] Service 프로덕션 코드 작성
  - [x] Controller 단위 테스트
  - [x] Controller 프로덕션 코드 작성


- Station 삭제 API - DELETE /stations/{id}
  - [x] 통합 테스트 작성
  - [x] Dao 단위 테스트 작성
  - [x] Dao 프로덕션 코드 작성
  - [x] Service 단위 테스트
  - [x] Service 프로덕션 코드 작성
  - [x] Controller 단위 테스트
  - [x] Controller 프로덕션 코드 작성

---
### 노선 관련 기능

- Line 등록 API - POST /lines
  - [x] 통합 테스트 작성
  - [x] Dao 단위 테스트 작성
  - [x] Dao 프로덕션 코드 작성
  - [x] Service 단위 테스트
  - [x] Service 프로덕션 코드 작성
  - [x] Controller 단위 테스트
  - [x] Controller 프로덕션 코드 작성


- Line 전체 조회 API - GET /lines
  - [x] 통합 테스트 작성
  - [x] Dao 단위 테스트 작성
  - [x] Dao 프로덕션 코드 작성
  - [x] Service 단위 테스트
  - [x] Service 프로덕션 코드 작성
  - [x] Controller 단위 테스트
  - [x] Controller 프로덕션 코드 작성


- Line 조회 API - GET /lines/{id}
  - [x] 통합 테스트 작성
  - [x] Dao 단위 테스트 작성
  - [x] Dao 프로덕션 코드 작성
  - [x] Service 단위 테스트
  - [x] Service 프로덕션 코드 작성
  - [x] Controller 단위 테스트
  - [x] Controller 프로덕션 코드 작성


[//]: # (우선 순위 낮음)
- Line 삭제 API - DELETE /lines/{id}
  - [ ] 통합 테스트 작성
  - [ ] Dao 단위 테스트 작성
  - [ ] Dao 프로덕션 코드 작성
  - [ ] Service 단위 테스트
  - [ ] Service 프로덕션 코드 작성
  - [ ] Controller 단위 테스트
  - [ ] Controller 프로덕션 코드 작성


- Line 에 Station 등록 API - PATCH /lines/{id}/register
  - [x] 통합 테스트 작성
  - [x] Dao 단위 테스트 작성
  - [x] Dao 프로덕션 코드 작성
  - [x] Service 단위 테스트
  - [x] Service 프로덕션 코드 작성
  - [x] Controller 단위 테스트
  - [x] Controller 프로덕션 코드 작성


- Line 에서 Station 제거 API - PATCH /lines/{id}/unregister
  - [x] 통합 테스트 작성
  - [x] Dao 단위 테스트 작성
  - [x] Dao 프로덕션 코드 작성
  - [x] Service 단위 테스트
  - [x] Service 프로덕션 코드 작성
  - [x] Controller 단위 테스트
  - [x] Controller 프로덕션 코드 작성

---
### 경로 관련 기능

- 최단 거리 조회 API - GET /path
  - [x] 통합 테스트 작성
  - [ ] 최단 경로 조회 단위 테스트
  - [ ] 최단 경로 조회 기능 구현
  - [ ] 요금 계산 단위 테스트
  - [ ] 요금 계산 기능 구현
  - [ ] Service 단위 테스트
  - [ ] Service 프로덕션 코드 작성
  - [ ] Controller 단위 테스트
  - [ ] Controller 프로덕션 코드 작성
