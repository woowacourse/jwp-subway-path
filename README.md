# jwp-subway-path

## Station API

### Station 등록

Request
```http
POST /stations HTTP/1.1
Host: localhost:8080
...

{
    "name": "동대구역"
}
```

Response
```http
HTTP/1.1 201 Created
```

### Stations 조회

Request
```http
GET /stations HTTP/1.1
Host: localhost:8080
...
```

Response
```http
HTTP/1.1 200 OK
...

[
    {
        "id": 1,
        "name": "신천역"
    },
    {
        "id": 2,
        "name": "동대구역"
    }
]
```

### 단일 Station 조회

Request
```http
GET /stations/2 HTTP/1.1
Host: localhost:8080
...
```

Response
```http
HTTP/1.1 200 OK
...

{
    "id": 2,
    "name": "동대구역"
}
```

### Station 수정

Request
```http
PUT /stations/1 HTTP/1.1
Host: localhost:8080
...
{
    "name": "용산역"
}
```

Response
```http
HTTP/1.1 200 OK
```

### Station 삭제

Request
```http
DELETE /stations/1 HTTP/1.1
Host: localhost:8080
...
```

Response
```http
HTTP/1.1 204 No Content
```

---

## Line API

### Line 등록

Request
```http
POST /lines HTTP/1.1
Host: localhost:8080
...

{
    "name": "1호선",
    "color": "bg-red-600"
}
```

Response
```http
HTTP/1.1 201 Created
```

### Lines 조회

Request
```http
GET /lines HTTP/1.1
Host: localhost:8080
...
```

Response
```http
HTTP/1.1 200 OK
...

[
    {
        "lineResponse": {
            "id": 1,
            "name": "1호선",
            "color": "bg-red-600"
        },
        "stationResponses": [
            {
                "id": 1,
                "name": "신천역"
            },
            {
                "id": 2,
                "name": "동대구역"
            }
        ],
        "sectionResponses": [
            {
                "upStationId": 1,
                "downStationId": 2,
                "distance": 10
            }
        ]
    },
    {
        "lineResponse": {
            "id": 2,
            "name": "2호선",
            "color": "bg-green-600"
        },
        "stationResponses": [
            {
                "id": 3,
                "name": "용산역"
            },
            {
                "id": 4,
                "name": "죽전역"
            }
        ],
        "sectionResponses": [
            {
                "upStationId": 3,
                "downStationId": 4,
                "distance": 10
            }
        ]
    }
]
```

### 단일 Line 조회

Request
```http
GET /lines/1 HTTP/1.1
Host: localhost:8080
...
```

Response
```http
HTTP/1.1 200 OK
...

{
    "lineResponse": {
        "id": 1,
        "name": "1호선",
        "color": "bg-red-600"
    },
    "stationResponses": [
        {
            "id": 1,
            "name": "신천역"
        },
        {
            "id": 2,
            "name": "동대구역"
        }
    ],
    "sectionResponses": [
        {
            "upStationId": 1,
            "downStationId": 2,
            "distance": 10
        }
    ]
}
```

### Line 수정

Request
```http
PUT /lines/1 HTTP/1.1
Host: localhost:8080
...
{
    "name": "3호선",
    "color": "bg-yellow-600"
}
```

Response
```http
HTTP/1.1 200 OK
```

### Line 삭제

Request
```http
DELETE /lines/1 HTTP/1.1
Host: localhost:8080
...
```

Response
```http
HTTP/1.1 204 No Content
```

---

## Section API

### Section 등록

Request
```http
POST /sections HTTP/1.1
Host: localhost:8080
...

{
    "lineId": 1,
    "upStationId": 1,
    "downStationId": 2,
    "distance": 10
}
```

Response
```http
HTTP/1.1 201 Created
```

### Section 조회

Request
```http
GET /sections?lineId=1 HTTP/1.1
Host: localhost:8080
...
```

Response
```http
HTTP/1.1 200 OK
...

[
    {
        "upStationId": 1,
        "downStationId": 3,
        "distance": 6
    },
    {
        "upStationId": 3,
        "downStationId": 2,
        "distance": 4
    }
]
```

### Section 삭제

Request
```http
DELETE /sections?lineId=1&stationId=2 HTTP/1.1
Host: localhost:8080
...
```

Response
```http
HTTP/1.1 204 No Content
```

---

## Path API

### Path 조회

Request
```http
GET /paths?upStationId=1&downStationId=3 HTTP/1.1
Host: localhost:8080
...
```

Response
```http
HTTP/1.1 200 OK
...

{
    "stations": [
        {
            "id": 1,
            "name": "청라언덕역"
        },
        {
            "id": 2,
            "name": "반월당역"
        },
        {
            "id": 3,
            "name": "경대병원역"
        }
    ],
    "distance": 16,
    "fare": 1450
}
```

---


## 비즈니스 규칙

- 노선에 역 등록
  - 등록 방식
    - 노선에 이미 등록되어있는 역을 기준으로 새로운 역을 등록합니다.
    - 기준이 되는 역이 없다면 등록할 수 없습니다.
    - 종점으로 등록 할 수 있다.
    - 노선에 역을 등록할 때, 거리 정보도 포함되어야 한다.
    - 노선에 등록된 역이 없는 경우, 두 역을 동시에 등록해야 한다.
    - 하나의 역은 여러 노선에 등록이 될 수 있다.
  - 갈래길 방지
    - 하나의 노선은 일직선으로 연결되야 합니다.
    - 상행역과 하행역이 이미 노선에 모두 등록되어 있다면 등록할 수 없습니다.
  - 거리 정보 관리
    - 거리는 양의 정수여야 한다.
    - 역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없습니다.

- 노선에서 역을 제거
  - 종점 제거
  - 재배치
    - 노선에 A - B - C 역이 연결되어 있을 때 B역을 제거할 경우 A - C로 재배치합니다.
    - A - C의 거리는 A - B, B - C 의 거리 합으로 정합니다.
  - 노선에 두 개 역
    - 노선에 등록된 역이 2개 인 경우 하나의 역을 제거할 때 두 역이 모두 제거되어야 합니다.

---

## 1단계 리팩토링 요구사항

- [x] API 명세 정리
- [x] Section ID 정보 삭제
- [x] 초기 데이터를 가지지 않도록 schema.sql에서 INSERT문 제거
- [x] Domain, Entity 분리
- [x] Service 내 메서드의 역할과 책임 분리
- [x] Line 조회 기능 변경
- [x] Collections.emptyList() 활용
- [x] exists 쿼리 활용
- [x] 조회 쿼리에서 필요한 데이터만 조회하도록 쿼리문 변경
- [x] 구간 정보 조회 기능 추가
- [x] @Transactional 적용
- [x] 테스트 시, Map 타입 대신 실제 요청 객체 생성해서 사용하도록 변경
- [x] @AutoConfigureTestDatabase의 default 옵션 제거
- [x] 가독성을 위해 개행 추가 및 제거
- [x] ExceptionHandler 설정 추가

---

## 2단계 기능 요구사항

### 0. 1단계 피드백 반영

- [x] `toEntity()` 메서드 이름 변경
- [x] DB 접근 최소화
- [x] Line 조회에 Section 정보를 포함하도록 수정
- [x] repository를 이용해 persistence layer와 서비스 로직 분리

### 1. DB 설정

- 데이터베이스 설정을 프로덕션과 테스트를 다르게 합니다.
  - 프로덕션의 데이터베이스는 로컬에 저장될 수 있도록 설정
  - 테스트용 데이터베이스는 인메모리로 동작할 수 있도록 설정

### 2. 경로 조회 API 구현

- 출발역과 도착역 사이의 최단 거리 경로를 구하는 API를 구현합니다.
- 최단 거리 경로와 함께 총 거리 정보를 함께 응답합니다.
- 한 노선에서 경로 찾기 뿐만 아니라 여러 노선의 환승도 고려합니다.

### 3. **요금 조회 기능 추가**

- 기본운임(10㎞ 이내): 기본운임 1,250원
- 이용 거리 초과 시 추가운임 부과
  - 10km~50km: 5km 까지 마다 100원 추가
  - 50km 초과: 8km 까지 마다 100원 추가

---

## 2단계 리팩토링 요구사항

- [x] SectionResponse에서 id가 아닌 StationResponse를 반환하도록 수정
- [x] 구현체 대신 인터페이스에 의존하도록 수정
- [x] 거리 계산 시 double로 계산하도록 수정
- [x] rowMapper에서 문자열 순서가 아닌 컬럼명에 의존하도록 수정
- [ ] PathService 테스트 케이스 추가
