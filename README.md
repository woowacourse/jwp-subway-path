# jwp-subway-path

### 미션 구현 순서

- API 레벨의 **통합 테스트를 구현**하여 내가 구현할 API의 큰 그림을 그리기
- **비즈니스 규칙**을 만족하는 **설계를 진행**하기
- 설계한 요구사항을 검증할 수 있는 **작은 단위의 테스트를 작성**하기
- 작은 단위의 **테스트를 만족시키는 코드를 구현**하기

## 1단계 기능 요구사항 분석

- API 설계

  | HTTP Method | URL | 설명 | HTTP Status | 
    |--- | --- | --- | --- |
  | get | /lines | 노선 목록 조회 | 200 |
  | get | /lines/{id} | 노선 조회 | 200 |
  | post | /lines/{line_id}/stations | 노선에 역 등록 (@RequestBody) | 200 |
  | delete | /lines/{line_id}/stations/{station_id} | 노선에 역 제거 | 204 |

- DB 설계
    - Line

      | id | name | color |
            | --- | --- | --- |
      | Long | String | String |

    - Station

      | id | name |
                  | --- | --- |
      | Long | String |

    - Section

      | id | line_id | departure_id | arrival_id | distance |
            | --- | --- | --- | --- | --- |
      | Long | Long | Long | Long | int |

<br>

- 객체 설계
    - Distance
        - int distance
            - 항상 양의 정수여야 한다.

    - Station
        - Long id
        - String name
      
    - Section
        - Long id
        - Station departure
        - Statoin arrival
        - Distance distance
      
    - LineInfo
        - Long id
        - String name
        - String color
      
    - Line
        - LineInfo lineInfo
        - List<Section> line

<br>

- 규칙
    - 정렬 순서 : 상행 → 하행
    - A-C-D에서
        - A-B 추가 : A-B-C-D
        - E-D 추가 : A-C-E-D
    - 노선에 최초에 역을 등록할 때는 항상 두 역을 동시에 등록해야한다.
    - 노선에 등록된 역이 2개인 경우, 하나의 역을 제거하면 두 역이 모두 제거 되어야한다.

### API별 프로세스

- SELECT 쿼리
    - SELECT section.id, section.line_id, departure_station.id, departure_station.name,
      arrival_station.id, arrival_station.name, section.distance
      FROM section
      LEFT JOIN station AS departure_station ON departure_station.id = section.departure_id
      LEFT JOIN station AS arrival_station ON arrival_station.id = section.arrival_id
  
- 노선 조회
    - GET ‘/lines/{id}’ 를 통해 노선의 ID 받아오기
    - SELECT 쿼리
    - List<Section>으로 변환
    - 상행 → 하행 순으로 탐색 후 순서대로 정렬
    - DTO 변환
  
- 노선 목록 조회
    - GET ‘/lines’
    - SELECT 쿼리
    - Map<Long,List<Section>> 으로 변환 → stream groupingBy
    - 노선별 상행 → 하행 순으로 탐색 후 순서대로 정렬
    - DTO 변환

- 노선에 역 등록
    - POST, ‘/lines/{line_id}/stations’
        - PathVariable id → 노선 ID
        - Requset body → 출발역, 도착역, 거리
    - 거리 검증
        - 양의 정수가 아니라면 예외 처리
    - findSectionByLineIdAndStationName()으로 해당 노선에서 각 역들이 포함된 Section 찾기
    - Section이 2개미만인지 검증
        - 한 노선에 Section이 2개 이상인 경우 → 예외처리
            - 순환 노선이 되거나 불가능한 케이스이기 때문에
        - 한 노선에 Section이 0개인 경우
            - 노선의 첫 등록 → 해당 노선에 섹션 갯수로 판단
            - 출발지와 도착지 모두 노선에 연결되지 않은 경우 → 예외 처리
    - Section의 거리와 새로 받아온 거리의 차가 양수인지 검증
    - 2개의 Section을 만들어주고 DB 저장
        - 공통된 출발지 (혹은 도착지)를 기준으로 새로운 도착지(혹은 출발지)를 위치시키고
          각 구간의 거리를 구하여 만들기
    - 기존의 section delete

- 노선에서 역 제거
    - DELETE ‘/lines/{line_id}/stations/{station_id}’
        - PathVariable line_id → 노선 ID / station_id → 역 ID
    - findSectionByLineIdAndStationId()로 섹션 찾기
        - 2개의 섹션 출발지와 도착지 고르기
        - 거리 구하기
        - 섹션 만들기
        - 저장하기
        - 찾은 섹션 2개 삭제

### 질문 사항

- DAO의 리턴값이 Entity여야할지, Domain이어야할지
    - 테이블들을 join하여 RowMapper로 Domain 객체를 반환
    - 간단하고, DB 커넥션의 수를 줄일 수 있기 때문에
    - 단점 : 계층 분리가 완벽하지 않음
