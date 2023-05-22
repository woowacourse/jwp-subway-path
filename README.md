# jwp-subway-path

## 요구 사항
### API 기능 요구사항
- [x] 노선에 역 등록 API 신규 구현
- [x] 노선에 역 제거 API 신규 구현
- [x] 노선 포함된 역 목록 조회 API 구현
  - [x] 노선에 포함된 역을 순서대로 보여주도록 응답
- [x] 경로 조회 api
 - [x] 역과 역 사이의 최단 경로와 그에 따른 요금을 응답
   - [x] 노선별 추가 요금 정책 적용
   - [x] 연령별 요금 할인 정책 적용

| 기능    | Method | URL                                    |
|-------|--------|----------------------------------------|
| 생성    | POST   | /lines/{line_id}/sections              |
| 삭제    | DELETE | /lines/{line_id}/sections/{station_id} |
| 목록 조회 | GET    | /lines/{line_id}/sections              |
| 경로 조회 | GET    | /paths/{station_id}/{station_id}}      |

### 환경
- [x] 프로덕션 db 로컬로 사용, 테스트 db는 인메모리로 사용

## 기능 목록
- [x] 노선에 역을 등록한다. 
- [x] 노선에 역을 등록할 때 거리 정보도 계산한다.
  - [x] 거리 정보는 양의 정수로 제한
  - [x] 노선 가운데 역이 등록 될 경우 거리 정보를 새롭게 계산해야 한다.
  - [x] 노선 가운데 역이 등록 될 경우, 가능한 거리를 초과한 경우 예외를 발생시킨다.  

- [x] 노선에서 역을 제거한다.
  - [x] 노선에서 역이 제거될 경우 역과 역 사이의 거리도 재배정되어야 한다. 
  - [x] 노선에서 역을 제거할 경우 정상 동작을 위해 재배치 되어야 한다.
  - [x] 노선에 등록된 역이 2개 인 경우 하나의 역을 제거할 때 두 역이 모두 제거되어야 한다.

- [x] 노선의 역들을 연결된 순으로 정렬

- [x] 역과 역 사이의 최단거리 구하기 기능
- [x] 요금 계산 기능
 - [x] 기본 거리비례제 정책 
  - 기본운임비 1250원
  - 이용 거리 초과 시 추가운임 부과
    10km~50km: 5km 까지 마다 100원 추가
    50km 초과: 8km 까지 마다 100원 추가
 - [x] 노선별 추가 요금 정책 
  - 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
  - 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
 - [x] 연령별 요금 할인 정책
  - 연령에 따른 요금 할인 정책을 반영
    - 청소년: 운임에서 350원을 공제한 금액의 20%할인
    - 어린이: 운임에서 350원을 공제한 금액의 50%할인
 
