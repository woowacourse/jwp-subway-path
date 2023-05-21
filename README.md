# jwp-subway-path

## 기능 요구사항

### 역
- [x] 역은 고유한 식별자를 가진다.
- [x] 역 이름은 `역`으로 끝나야한다.
- [x] 역 이름은 2글자에서 11글자까지 가능하다.
- [x] 역 이름은 한글 + 숫자로만 이루어져야한다.

### 구역
- [x] 구역은 두 역과 역사이의 거리를 가진다.
- [x] 거리는 0 이상의 정수이고, 단위는 km이다.

### 노선
- [x] 노선 이름은 숫자 + `호선` 이다.
  - [x] 숫자는 1 ~ 9까지 가능하다.

- [x] 노선의 색은 `색`으로 끝나야한다.
  - [x] 색 이름은 2글자에서 11글자까지 가능하다.
  
- [x] 노선은 추가 요금을 가진다.

- [x] 역을 추가할 수 있어야 한다.
  -[x] 역을 추가할 때, 상행, 하행 역 정보와 거리 정보를 입력받는다.
   - 최초 등록이 아닐 경우, 상행역 또는 하행역 어느 한가지도 존재하지 않으면 예외를 던진다.
  -[x] 하나의 역은 여러 노선에 등록될 수 있다.
  -[x] 두 역의 가운데에 다른 역을 등록할 때, 기존 거리를 고려해야한다.
  
- [x] 역이 2개 이상 등록된 노선을 전부 보여주어야한다.

- [x] 구역은 역 순서대로 저장되어 있어야 한다.
  - [x] 노선 번호를 입력받으면, 노선에 포함된 역을 순서대로 보여주어야한다.

- [x] 역을 제거할 수 있어야 한다.
  -[x] 역을 제거할 경우 남은 역을 재배치 해야한다.
  -[x] 노선에서 역이 제거될 경우 역과 역 사이의 거리도 재배정되어야한다.
  -[x] 노선에 등록된 역이 2개 인 경우 하나의 역을 제거할 때 두 역이 모두 제거되어야한다.

### 노선도
- [x] 출발역과 도착역을 넣으면 최단 거리 경로 정보를 반환해야한다.
  - [x] 거리 정보도 함께 반환해야한다.

### 요금 정책
- [x] 거리 정보를 통해 요금을 계산할 수 있다.
  - 기본 운임은 1250원이다.
    - 추가운임은 밑 조건에 따라 결정된다. 
      - 거리 10 ~ 50km 까지는 5km 마다 100원 추가
      - 거리가 50km 초과할 때 부터는 8km 마다 100원 추가
- [x] 이용한 노선 정보를 통해 요금을 계산할 수 있다.
  - 노선의 추가 요금 중 제일 비싼 요금이 추가 요금이 된다.
  #### 할인 정책
    - [x] 할인 정책을 통해 할인된 요금을 계산할 수 있다.
      - 영유아: 6세 미만이라면 100% 할인
      - 어린이: 6세 이상~13세 미만이라면 운임에서 350원을 공제한 금액의 50%할인
      - 청소년: 13세 이상~19세 미만이라면 운임에서 350원을 공제한 금액의 20%할인



---

## API 명세


**역 등록**

request
```http request
POST /stations
...
{
    "name" : "잠실역"
}
```

response
```http request
201 Created
Location: /stations/{id}
```

**역 정보 조회**

request
```http request
GET /stations/{id}
```

response
```http request
200 OK
{
    "id": 1,
    "name": "잠실역"
}
```

**노선 등록**

request
```http request
POST /lines
{
    "name": "2호선",
    "color": "초록색"
}
```

response
```http request
201 Created
Location: /lines/{id}
```

**노선 조회**

request
```http request
GET /lines/{id}
```

response
```http request
200 OK
{
    "id": 1,
    "name": "2호선",
    "color": "초록색",
    "fare": 300,
    "stations": [
        {
            "id": 1,
            "name": "잠실역"
        },
        {
            "id": 2,
            "name": "잠실새내역"
        }
    ]
}
```

**노선 목록 조회**

request
```http request
GET /lines
```

response
```http request
200 OK
{
    "lines": [
        {
            "id": 1,
            "name": "2호선",
            "color": "초록색",
            "fare": 300,
            "stations": [
                {
                    "id": 1,
                    "name": "잠실역"
                },
                {
                    "id": 2,
                    "name": "잠실새내역"
                },
            ]
        },
        {
            "id": 2,
            "name": "4호선",
            "color": "하늘색",
            "stations": [
                {
                    "id": 3,
                    "name": "이수역"
                },
                {
                    "id": 4,
                    "name": "서울역"
                },
            ]
        }
    ]
}
```

**노선 역 등록**

request
```http request
POST /lines/{id}/sections
{
    "upwardStationId": 1,
    "downwardStationId": 2,
    "distance": 10
}
```

response
```http request
201 Created
Location: /lines/{id}
```

**노선 역 제거**

request
```http request
DELETE /lines/{lineId}?stationId={stationId}
```

response
```http request
204 No Content
```

**경로 조회**

request
```http request
GET /subways/shortest-paths?sourceStationId={sourceStationId}&destinationStationId={destinationStationId}&passengerAge={passengerAge}
```

response
```http request
200 OK
{
    "stations" : [
        {
            "id" : 1,
            "name" "잠실역"
        },
        {
            "id" : 2,
            "name" "잠실새내역"
        },
        {
            "id" : 3,
            "name" "종합운동장역"
        }
    ],
    "distance" : 13,
    "fare" : 3000 
}
```




