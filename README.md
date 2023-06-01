# jwp-subway-path

## 1단계 요구사항

### 기능 요구사항

- [x] 노선 추가
- [x] 노선 조회
  - [x] 노선에 포함된 역을 순서대로 보여주도록 한다.
- [x] 노선 목록 조회
  - [x] 노선에 포함된 역을 순서대로 보여주도록 한다.
- [x] 역 추가
- [x] 역 삭제
  - [x] 역을 제거한 후 노선을 재배치한다.
  - [x] 역을 제거한 후 제거된 역의 전 역, 다음 역의 거리를 업데이트한다.
  - [x] 역을 제거한 후 노선에 역이 하나 남으면 해당 역도 제거한다.
- [x] 구간 추가
  - [x] 역이 등록될 때 양의 정수인 거리 정보가 포함되어야 한다.
  - [x] 노선에 역이 하나도 없을 때 두 역을 동시에 등록해야 한다.
  - [x] 하나의 역은 여러 개의 노선에 등록될 수 있다.
  - [x] 존재하는 역의 다음 역을 추가하면 중간에 역이 등록될 수 있다.
  - [x] 노선 사이에 역이 등록되는 경우 등록된 역을 포함한 전 역, 다음 역의 거리를 업데이트 한다.
  - [x] 노선 사이에 역이 등록되는 경우 새로 추가되는 역과 이전 역의 거리는 기존 역 사이의 거리보다 작아야 한다.
  - [x] 기준이 되는 역이 없으면 등록할 수 없다.

### 리팩토링 목록

- [x] 역 추가, 구간 추가 기능 분리
  - [x] 역 추가 이후 연결할 구간을 추가하도록 변경
  - [x] StationService, SectionService 분리
- [x] GlobalControllerAdvice 클래스 리팩토링
  - [x] CustomException 필드 변경
  - [x] CustomException 클래스 네이밍 변경
- [x] Station DB table 컬럼 리팩토링
- [x] Station 객체가 id를 갖도록 리팩토링
- [x] StationService saveNewSections 메소드 내부 로직 변경 -> batchInsert문 사용
- [x] Service, DAO isExisted 메소드 내부 로직 변경
- [x] Test 코드 내부 예외 검증 시 예외 메시지도 함께 검증
- [x] Controller 구간 추가 후 Location 헤더 값에 해당하는 노선 조회할 수 있는 값 지정해주기
- [x] SectionService 조회 메서드 make -> convertTo...ByEntity로 네이밍 변경
- [x] SectionService 새로운 구간 등록, 삭제 할 때 업데이트 된 구간 DB에 저장하는 로직 변경
- [x] Section validateDistance 메서드 접근제어자 변경 및 책임 분리
- [x] Test 코드 내부 DisplayName 더 명확하게 수정
- [x] Test 코드 내부 테스트를 위한 사전 데이터 준비 과정의 중복 코드 제거
- [x] Test 코드 내부 `@Nested` 어노테이션을 통해 테스트 코드 가독성 증진

---

## 2단계 요구사항

### 프로그래밍 요구사항

- [x] 데이터베이스 설정을 프로덕션과 테스트를 다르게 지정
  - [x] 프로덕션의 데이터베이스는 로컬에 저장될 수 있도록 설정
  - [x] 테스트용 데이터베이스는 인메모리로 동작할 수 있도록 설정

### 기능 요구사항

- [x] 경로 조회 API 구현
  - [x] 출발역과 도착역 사이의 최단 거리 경로를 구하는 API 구현
  - [x] 응답에는 최단 거리 경로와 총 거리 정보 포함한다.
  - [x] 여러 노선을 걸처 최단 경로가 나올 수 있다.
- [x] 요금 조회 기능 추가
  - [x] 경로 조회 시 요금 정보를 포함하여 응답
- [x] 요금 계산 기능 추가
  - [x] 기본운임(10㎞ 이내): 기본운임 1,250원
    - [x] 이용 거리 초과 시 추가운임 부과
    - [x] 10km~50km: 5km 까지 마다 100원 추가
    - [x] 50km 초과: 8km 까지 마다 100원 추가

### 리팩토링 목록

- [x] Repository 생성
  - [x] LineRepository
  - [x] SectionRepository
  - [x] StationRepository
- [x] 최단 경로 조회 API
  - [x] url 변경
  - [x] 응답 형식 변경
- [x] 최단 경로 조회 기능
  - [x] getDijkstraShortestPath 메소드 추상화
  - [x] getDijkstraShortestPath 메소드 sourceStation과 targetStation이 같은 경우 예외 처리 기능 구현
  - [x] Vertex로 역(Station) 객체 사용하도록 리팩토링
  - [x] Section 도메인에 외부 라이브러리에 대한 의존 끊기
- [x] 요금 계산 기능
  - [x] 요금 계산 기능 책임 분리
  - [x] 거리가 0 이하일 때 예외 처리 기능 구현
- [ ] 테스트
  - [x] Fare 테스트 추가
  - [x] ShortestPath 테스트 추가
  - [x] ShortestPath Integration 테스트 추가

---

## API 명세서

### 노선 API

| Method | URI         | Description |
|--------|-------------|-------------|
| POST   | /lines      | 노선 추가       |
| GET    | /lines      | 전체 노선과 역 조회 |
| GET    | /lines/{id} | 특정 노선과 역 조회 |

### 역 API

| Method | URI       | Description |
|--------|-----------|-------------|
| POST   | /stations | 역 추가        |

### 구간 API

| Method | URI       | Description |
|--------|-----------|-------------|
| POST   | /sections | 노선에 역 추가    |
| DELETE | /sections | 노선에 역 삭제    |

### 경로 조회 API

| Method | URI             | Description   |
|--------|-----------------|---------------|
| GET    | /paths/shortest | 최단 경로 및 요금 조회 |

---

### Line API 요청 / 응답 예시

#### POST : 노선 추가

`Request`

```http request
POST /lines HTTP/1.1
Host: localhost:8080

{
    "name" : "1호선"
}
```

`Response`

``` http request
HTTP/1.1 201 Created
Content-Type: application/json
Location: /lines/1
```

#### GET : 노선 목록 조회

`Request`

```http request
GET /lines HTTP/1.1
Host: localhost:8080
```

`Response`

``` http request
HTTP/1.1 200
Content-Type: application/json

[
    {
        "id" : 1,
        "name" : "2호선"
        "stations" : [
            {
                "name" : "잠실역"
            }
        ]
    },
    {
        "id" : 2,
        "name" : "8호선"
        "stations" : [
            {
                "name" : "잠실역"
            },
            {
                "name" : "석촌역"
            }
        ]
    }
]
```

#### GET : 노선 조회

`Request`

```http request
GET /lines/{id} HTTP/1.1
Host: localhost:8080
```

`Response`

``` http request
HTTP/1.1 200
Content-Type: application/json

{
    "name" : "2호선"
    "stations" : [
        {
            "name" : "잠실역"
        }
    ]
}
```

---

### Station API 요청 / 응답 예시

#### POST : 역 추가

`Request`

```http request
POST /stations HTTP/1.1
Host: localhost:8080

{
    "name" : "잠실역"
}
```

`Response`

``` http request
HTTP/1.1 201 Created
Content-Type: application/json
Location: /stations/1
```

---

### Section API 요청 / 응답 예시

#### POST : 노선에 역 추가

`Request`

```http request
POST /sections HTTP/1.1
Host: localhost:8080

{
    "upStation" : "잠실역",
    "downStation" : "잠실새내역",
    "distance" : 10,
    "lineId" : 1
}
```

`Response`

``` http request
HTTP/1.1 201 Created
Content-Type: application/json
Location: /sections/1
```

#### DELETE : 노선에 역 삭제

`Request`

```http request
DELETE /sections HTTP/1.1
Host: localhost:8080

{
    "name" : "잠실역"
}
```

`Response`

``` http request
HTTP/1.1 204 No Content
```

---

### Path API 요청 / 응답 예시

#### GET : 최단 경로 및 요금 조회

`Request`

```http request
GET /paths/shortestPath HTTP/1.1
Host: localhost:8080

{
    "sourceStation":"주노역",
    "targetStation":"찰리역"
}
```

`Response`

``` http request
HTTP/1.1 200 OK
Content-Type: application/json

{
    "shortestPath": [
        {
            "id": 6,
            "name": "찰리역"
        },
        {
            "id": 4,
            "name": "주노역"
        }
    ],
    "distance": 9,
    "fare": 1250
}
```
