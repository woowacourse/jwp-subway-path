# jwp-subway-path

### 미션 구현 순서

- API 레벨의 **통합 테스트를 구현**하여 내가 구현할 API의 큰 그림을 그리기
- **비즈니스 규칙**을 만족하는 **설계를 진행**하기
- 설계한 요구사항을 검증할 수 있는 **작은 단위의 테스트를 작성**하기
- 작은 단위의 **테스트를 만족시키는 코드를 구현**하기

## 1단계 기능 요구사항 분석

### API 설계

| HTTP Method | URL                                    | 설명                      | HTTP Status |
|-------------|----------------------------------------|-------------------------|-------------|
| get         | /lines                                 | 노선 목록 조회                | 200         |
| get         | /lines/{id}                            | 노선 조회                   | 200         |
| post        | /lines/{line_id}/stations              | 노선에 역 등록 (@RequestBody) | 200         |
| delete      | /lines/{line_id}/stations/{station_id} | 노선에 역 제거                | 204         |

### Line Table 설계

| id   | name   | color  |
|------|--------|--------|
| Long | String | String |

### Station Table 설계

| id   | name   |
|------|--------|
| Long | String |

### Section Table 설계

| id   | line_id | departure_id | arrival_id | distance |
|------|---------|--------------|------------|----------|
| Long | Long    | Long         | Long       | int      |

### 객체 설계

- [x] Distance
    - `int distance`
        - 항상 양의 정수여야 한다.

- [x] Station
    - `Long id`
    - `String name`

- [x] Section
    - `Long id`
    - `Station departure`
    - `Statoin arrival`
    - `Distance distance`

- [x] LineInfo
    - `Long id`
    - `String name`
    - `String color`

- [x] Line
    - `LineInfo line`
    - `List<Section> line`

- [x] SectionSorter
    - `List<Section>`를 상행에서 하행 순으로 정렬

### API별 프로세스

- SELECT 쿼리
  ```SQL
      SELECT sections.id, sections.line_id, departure_station.id, departure_station.name,
      arrival_station.id, arrival_station.name, sections.distance
      FROM sections
      LEFT JOIN station AS departure_station ON departure_station.id = section.departure_id
      LEFT JOIN station AS arrival_station ON arrival_station.id = section.arrival_id
  ```

- [x] 노선 조회
    - `GET /lines/{id}` 를 통해 노선의 ID 받아오기
    - SELECT 쿼리
    - `List<Section>`으로 변환
    - 상행 → 하행 순으로 탐색 후 순서대로 정렬
    - DTO 변환

- [x] 노선 목록 조회
    - `GET /lines`
    - SELECT 쿼리
    - `Map<Long,List<Section>>` 으로 변환 → `stream groupingBy`
    - 노선별 상행 → 하행 순으로 탐색 후 순서대로 정렬
    - DTO 변환

- [x] 노선에 역 등록
    - `POST /lines/{line_id}/stations`
        - `@PathVariable Long id` → 노선 ID
        - `@RequsetBody` → 출발역, 도착역, 거리
    - 거리 검증
        - 양의 정수가 아니라면 예외 처리
    - DB에 해당 역 존재 여부 확인
      - 없으면 등록하기
    - `findSectionByLineId()`으로 해당 노선에서 각 역들이 포함된 Section 찾기
    - Section의 개수 검증
        - 0개인 경우 다른 검증 없이 등록
    - 등록 출발지의 section 갯수와 등록 도착지의 section 갯수 확인 후 예외 처리
        - (0개, 0개)인 경우 : 연결될 수 없는 노선이므로 예외 처리
        - (1개, 1개)인 경우 : 순환 노선이므로 예외 처리
        - (1개 이상, 2개) 혹은 (2개, 1개 이상)인 경우 : 기존재구간이므로 예외 처리
        - (1개 이상, 0개) 혹은 (0개, 1개 이상)인 경우 : 정상 케이스
    - 비교할 Section 선정
    - Section의 거리와 새로 받아온 거리의 차가 양수인지 검증
    - 연결된 역 찾기
    - 2개의 Section을 만들어주고 DB 저장
        - 공통된 출발지 (혹은 도착지)를 기준으로 새로운 도착지(혹은 출발지)를 위치시키고
          각 구간의 거리를 구하여 만들기
    - 기존의 section delete

- [x] 노선에서 역 제거
    - `DELETE /lines/{line_id}/stations/{station_id}`
        - `@PathVariable line_id` → 노선 ID / `station_id` → 역 ID
    - `findSectionByLineIdAndStationId()`로 섹션 찾기
        - 2개의 section 출발지와 도착지 고르기
        - 거리 구하기
        - section 만들기
        - 저장하기
        - 찾은 section 2개 삭제

<br>

### 규칙

- 정렬 순서 
  - 상행 → 하행
- 노선에 최초에 역을 등록
  - 항상 두 역을 동시에 등록해야한다.
- 노선에 등록된 역이 2개인 경우
  - 하나의 역을 제거하면 두 역이 모두 제거 되어야한다.
- 역 추가 시 발생할 수 있는 케이스 정리 (노선 : A-B-C-D-E-F)
    - 순환 노선 :
        - 등록 출발지가 한 섹션에 포함 && 등록 도착지가 한 섹션에 포함
            - 상행 종점과 하행 종점이 연결된 경우 (F-A)
    - 존재 구간 추가
        - 등록 출발지가 한 섹션에 포함 && 등록 도착지가 두 섹션에 포함
            - 상행 종점을 포함한 기존재 구간 (A-B 등)
        - 등록 출발지가 두 섹션에 포함 && 등록 도착지가 한 섹션에 포함
            - 하행 종점을 포함한 기존재 구간 (E-F 등)
        - 등록 출발지가 두 섹션에 포함 && 등록 도착지가 두 섹션에 포함
            - 출발지와 도착지 모두 종점이 아닌 기 존재 구간 (B-C 등)
    - 종점 추가
        - 등록 출발지가 섹션에 미포함 && 등록 도착지가 한 섹션에 포함
            - 상행 종점 추가 (G-A)
        - 등록 출발지가 한 섹션에 포함 && 등록 도착지가 섹션에 미포함
            - 하행 종점 추가 (F-G)
    - 정상 케이스
        - 등록 출발지가 한 섹션에 포함 && 등록 도착지가 섹션에 미포함
            - 출발지가 상행 종점인 경우 (A-G)
        - 등록 출발지가 섹션에 미포함 && 등록 도착지가 한 섹션에 포함
            - 도착지가 하행 종점인 경우 (G-F)
        - 등록 출발지가 두 섹션에 포함 && 등록 도착지가 섹션에 미포함
            - 출발지가 종점이 아닌 경우 (B-G 등)
        - 등록 출발지가 섹션에 미포함 && 등록 도착지가 두 섹션에 포함
            - 도착지가 종점이 아닌 경우 (G-B 등)

### 질문 사항

- DAO의 리턴값이 Entity여야할지, Domain이어야할지
    - 테이블들을 join하여 RowMapper로 Domain 객체를 반환
    - 간단하고, DB 커넥션의 수를 줄일 수 있기 때문에
    - 단점 : 계층 분리가 완벽하지 않음


## 2단계

### 프로그래밍 요구사항
- 데이터베이스 설정을 프로덕션과 테스트를 다르게 합니다.
  - 프로덕션의 데이터베이스는 로컬에 저장될 수 있도록 설정
  - 테스트용 데이터베이스는 인메모리로 동작할 수 있도록 설정

### 기능 요구 사항
- [x] 경로 조회 API 구현
  - 최적 경로 dijkstra 알고리즘 적용
- [x] 요금 조회 기능 추가
  - 최적경로 반환시 요금 반환

| HTTP Method | URL                                 | 설명                       | HTTP Status |
|-------------|-------------------------------------|--------------------------|-------------|
| get         | /route/search?departure=?&arrival=? | 경로 조회 API 구현             | 200         |

- 요금
  - 기본운임(10㎞ 이내): 기본운임 1,250원
  - 이용 거리 초과 시 추가운임 부과
    - 10km~50km: 5km 까지 마다 100원 추가
    - 50km 초과: 8km 까지 마다 100원 추가
